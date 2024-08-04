package com.likelion.scul.board.service;

import com.likelion.scul.board.dto.*;
import com.likelion.scul.board.service.S3Service;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.board.domain.*;
import com.likelion.scul.board.repository.*;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.repository.SportsRepository;
import com.likelion.scul.common.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final SportsRepository sportsRepository;
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public PostService(UserRepository userRepository, BoardRepository boardRepository, PostRepository postRepository,
                       TagRepository tagRepository, SportsRepository sportsRepository, ImageRepository imageRepository,
                       S3Service s3Service) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.sportsRepository = sportsRepository;
        this.imageRepository = imageRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public void createPost(PostRequestDto postRequestDto, String email) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Sports 찾기
        Sports sports = sportsRepository.findBySportsName(postRequestDto.getSportsName())
                .orElseThrow(() -> new RuntimeException("Sports not found for given sportsName"));

        // Board 찾기: boardName과 sportsId를 함께 사용
        Board board = boardRepository.findByBoardNameAndSportsSportsId(postRequestDto.getBoardName(), sports.getSportsId())
                .orElseThrow(() -> new RuntimeException("Board not found for given boardName and sportsId"));

        Tag tag = tagRepository.findByTagName(postRequestDto.getTagName())
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        LocalDateTime createdDateTime = LocalDateTime.parse(postRequestDto.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME);

        Post post = new Post(board, tag, user, postRequestDto.getPostTitle(), postRequestDto.getPostContent(), createdDateTime, 0);
        postRepository.save(post);

        if (postRequestDto.getFiles() != null) {
            for (MultipartFile file : postRequestDto.getFiles()) {
                String key = s3Service.uploadFile(file);
                String imageUrl = s3Service.getFileUrl(key).toString();
                Image postImage = new Image(imageUrl, post);
                imageRepository.save(postImage);
            }
        }
    }

    @Transactional
    public void updatePost(PostUpdateRequestDto postUpdateRequestDto, String email) throws IOException {
        Post post = postRepository.findById(postUpdateRequestDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("User not authorized to update this post");
        }

        Board board = boardRepository.findByBoardName(postUpdateRequestDto.getBoardName())
                .orElseThrow(() -> new RuntimeException("Board not found"));
        Tag tag = tagRepository.findByTagName(postUpdateRequestDto.getTagName())
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        Sports sports = sportsRepository.findBySportsName(postUpdateRequestDto.getSportsName())
                .orElseThrow(() -> new RuntimeException("Sports not found"));

        LocalDateTime createdDateTime = LocalDateTime.parse(postUpdateRequestDto.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME);

        post.setBoard(board);
        post.setTag(tag);
        post.setPostTitle(postUpdateRequestDto.getPostTitle());
        post.setPostContent(postUpdateRequestDto.getPostContent());
        post.setCreatedAt(createdDateTime);

        List<Image> existingImages = imageRepository.findByPost(post);
        for (Image image : existingImages) {
            String key = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
            s3Service.deleteFile(key);
            imageRepository.delete(image);
        }

        for (MultipartFile file : postUpdateRequestDto.getFiles()) {
            String key = s3Service.uploadFile(file);
            String imageUrl = s3Service.getFileUrl(key).toString();
            Image postImage = new Image(imageUrl, post);
            imageRepository.save(postImage);
        }
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("User not authorized to delete this post");
        }

        List<Image> images = imageRepository.findByPost(post);
        for (Image image : images) {
            String key = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
            s3Service.deleteFile(key);
            imageRepository.delete(image);
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostDetailDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setPostView(post.getPostView() + 1);
        postRepository.save(post);

        List<String> imageUrls = imageRepository.findByPost(post).stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        List<CommentDto> comments = post.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getCommentId(),
                        comment.getUser().getNickname(),
                        comment.getCommentContent(),
                        LocalDateTime.parse(comment.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME)
                ))
                .collect(Collectors.toList());

        return new PostDetailDto(
                post.getPostId(),
                post.getUser().getNickname(),
                post.getBoard().getBoardName(),
                post.getTag().getTagName(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getCreatedAt(),
                post.getPostView(),
                imageUrls,
                post.getLikes().size(),
                comments
        );
    }

    @Transactional(readOnly = true)
    public PostListResponseDto getPostList(PostListRequestDto postListRequestDto) {


        List<Post> filteredPosts = postRepository.findAll()
                .stream()
                .filter(post -> post.getBoard().getSports().getSportsName().equals(postListRequestDto.sportsName())) // Board의 sports로 필터링
                .filter(post -> post.getBoard().getBoardName().equals(postListRequestDto.boardName()))
                .filter(post -> {
                    if ("전체".equals(postListRequestDto.tagName())) {
                        return true; // 전체일 경우 모든 태그 포함
                    } else {
                        return post.getTag().getTagName().equals(postListRequestDto.tagName());
                    }
                })
                .collect(Collectors.toList());


        // 필터링된 게시물 목록을 페이징 처리
        if (postListRequestDto.searchContent() != null && !postListRequestDto.searchContent().trim().isEmpty()) {
            switch (postListRequestDto.searchType()) {
                case "제목":
                    filteredPosts = filteredPosts.stream()
                            .filter(post -> post.getPostTitle().contains(postListRequestDto.searchContent()))
                            .collect(Collectors.toList());
                    break;
                case "내용":
                    filteredPosts = filteredPosts.stream()
                            .filter(post -> post.getPostContent().contains(postListRequestDto.searchContent()))
                            .collect(Collectors.toList());
                    break;
                case "작성자":
                    filteredPosts = filteredPosts.stream()
                            .filter(post -> post.getUser().getNickname().contains(postListRequestDto.searchContent()))
                            .collect(Collectors.toList());
                    break;
            }
        }

        switch (postListRequestDto.sortMethod()) {
            case "최신 순":
                filteredPosts = filteredPosts.stream()
                        .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                        .collect(Collectors.toList());
                break;
            case "조회 순":
                filteredPosts = filteredPosts.stream()
                        .sorted(Comparator.comparing(Post::getPostView).reversed())
                        .collect(Collectors.toList());
                break;
            case "댓글 순":
                filteredPosts = filteredPosts.stream()
                        .sorted(Comparator.comparing(post -> post.getComments().size(), Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                break;
        }

        System.out.println("--------------------");
        for (Post filteredPost : filteredPosts) {
            System.out.println(filteredPost.getPostTitle());
        }
        System.out.println("--------------------");

        int start = (postListRequestDto.page() - 1) * 14;
        int end = Math.min(start + 14, filteredPosts.size());
        List<PostListDto> paginatedPosts = filteredPosts.subList(start, end).stream()
                .map(post -> new PostListDto(
                        post.getPostId(),
                        post.getUser().getNickname(),
                        post.getTag().getTagName(),
                        post.getPostTitle(),
                        post.getCreatedAt(),
                        post.getLikes().size(),
                        post.getPostView(),
                        post.getComments().size(),
                        post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl()
                ))
                .collect(Collectors.toList());


        int totalPosts = filteredPosts.size(); // 필터 조건을 만족하는 총 게시물 수 계산
        return new PostListResponseDto(paginatedPosts, totalPosts);
    }
    @Transactional(readOnly = true)
    public List<PostListDto> getRecentPosts() {
        List<Post> posts = postRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return posts.stream()
                .map(post -> new PostListDto(
                        post.getPostId(),
                        post.getUser().getNickname(),
                        post.getTag().getTagName(),
                        post.getPostTitle(),
                        post.getCreatedAt(),
                        post.getLikes().size(),
                        post.getPostView(),
                        post.getComments().size(),
                        post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostListDto> getHotPosts() {
        List<Post> posts = postRepository.findAll().stream()
                .sorted(Comparator.comparing((Post post) -> post.getLikes().size()).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return posts.stream()
                .map(post -> new PostListDto(
                        post.getPostId(),
                        post.getUser().getNickname(),
                        post.getTag().getTagName(),
                        post.getPostTitle(),
                        post.getCreatedAt(),
                        post.getLikes().size(),
                        post.getPostView(),
                        post.getComments().size(),
                        post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
