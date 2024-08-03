package com.likelion.scul.board.service;

import com.likelion.scul.board.dto.PostDetailDto;
import com.likelion.scul.board.dto.PostListDto;
import com.likelion.scul.board.dto.PostRequestDto;
import com.likelion.scul.board.dto.PostUpdateRequestDto;
import com.likelion.scul.board.dto.PostListRequestDto;
import com.likelion.scul.board.service.S3Service;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.board.domain.*;
import com.likelion.scul.board.dto.CommentDto;
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

        Board board = boardRepository.findByBoardName(postRequestDto.getBoardName())
                .orElseThrow(() -> new RuntimeException("Board not found"));
        Tag tag = tagRepository.findByTagName(postRequestDto.getTagName())
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        Sports sports = sportsRepository.findBySportsName(postRequestDto.getSportsName())
                .orElseThrow(() -> new RuntimeException("Sports not found"));

        LocalDateTime createdDateTime = LocalDateTime.parse(postRequestDto.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME);

        Post post = new Post(board, tag, sports, user, postRequestDto.getPostTitle(), postRequestDto.getPostContent(), createdDateTime, 0);
        postRepository.save(post);

        for (MultipartFile file : postRequestDto.getFiles()) {
            String key = s3Service.uploadFile(file);
            String imageUrl = s3Service.getFileUrl(key).toString();
            Image postImage = new Image(imageUrl, post);
            imageRepository.save(postImage);
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
    public List<PostListDto> getPostList(PostListRequestDto postListRequestDto) {

        System.out.println("postListRequestDto.boardName() = " + postListRequestDto.boardName());
        System.out.println("postListRequestDto.tagName() = " + postListRequestDto.tagName());
        System.out.println("postListRequestDto = " + postListRequestDto.sportsName());

        List<Post> posts = postRepository.findAll().stream()
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

        for (Post post : posts) {
            System.out.println("post.getPostTitle() = " + post.getPostTitle());
        }

        // 수정된 부분: searchContent가 null이거나 빈 문자열일 경우 필터링을 건너뜁니다.
        if (postListRequestDto.searchContent() != null && !postListRequestDto.searchContent().trim().isEmpty()) {
            switch (postListRequestDto.searchType()) {
                case "제목":
                    posts = posts.stream()
                            .filter(post -> post.getPostTitle().contains(postListRequestDto.searchContent()))
                            .collect(Collectors.toList());
                    break;
                case "내용":
                    posts = posts.stream()
                            .filter(post -> post.getPostContent().contains(postListRequestDto.searchContent()))
                            .collect(Collectors.toList());
                    break;
                case "작성자":
                    posts = posts.stream()
                            .filter(post -> post.getUser().getNickname().contains(postListRequestDto.searchContent()))
                            .collect(Collectors.toList());
                    break;
            }
        }

        switch (postListRequestDto.sortMethod()) {
            case "최신 순":
                posts = posts.stream()
                        .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                        .collect(Collectors.toList());
                break;
            case "조회 순":
                posts = posts.stream()
                        .sorted(Comparator.comparing(Post::getPostView).reversed())
                        .collect(Collectors.toList());
                break;
            case "댓글 순":
                posts = posts.stream()
                        .sorted(Comparator.comparing(post -> post.getComments().size(), Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                break;
        }

        int start = (postListRequestDto.page() - 1) * 14;
        int end = Math.min(start + 14, posts.size());
        posts = posts.subList(start, end);

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
