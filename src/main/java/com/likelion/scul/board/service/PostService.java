package com.likelion.scul.board.service;

import com.likelion.scul.board.dto.PostDetailDto;
import com.likelion.scul.board.dto.PostListDto;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.board.domain.*;
import com.likelion.scul.board.dto.CommentDto;
import com.likelion.scul.board.repository.*;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.repository.SportsRepository;
import com.likelion.scul.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SportsRepository sportsRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public void createPost(String boardName, String tagName, String sportsName, String postTitle, String postContent, String createdAt, List<String> imageUrls, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = boardRepository.findByBoardName(boardName)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        Sports sports = sportsRepository.findBySportsName(sportsName)
                .orElseThrow(() -> new RuntimeException("Sports not found"));

        LocalDateTime createdDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);

        Post post = new Post(board, tag, sports, user, postTitle, postContent, createdDateTime, 0);
        postRepository.save(post);

        for (String imageUrl : imageUrls) {
            Image postImage = new Image(imageUrl, post);
            imageRepository.save(postImage);
        }
    }

    @Transactional
    public void updatePost(Long postId, String boardName, String tagName, String sportsName, String postTitle, String postContent, String createdAt, List<String> imageUrls, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!post.getUser().getEmail().equals(email)) {
            throw new RuntimeException("User not authorized to update this post");
        }

        Board board = boardRepository.findByBoardName(boardName)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        Tag tag = tagRepository.findByTagName(tagName)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        Sports sports = sportsRepository.findBySportsName(sportsName)
                .orElseThrow(() -> new RuntimeException("Sports not found"));

        LocalDateTime createdDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);

        // 엔티티를 새로 생성하지 않고, 필드를 업데이트합니다.
        post.setBoard(board);
        post.setTag(tag);
        post.setSports(sports);
        post.setPostTitle(postTitle);
        post.setPostContent(postContent);
        post.setCreatedAt(createdDateTime);
        // postRepository.save(post); // 트랜잭션이 커밋될 때 자동으로 저장됩니다.

        // 기존 이미지 삭제
        List<Image> existingImages = imageRepository.findByPost(post);
        for (Image image : existingImages) {
            imageRepository.delete(image);
        }

        // 새로운 이미지 저장
        for (String imageUrl : imageUrls) {
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

        // 관련된 이미지 삭제
        List<Image> images = imageRepository.findByPost(post);
        for (Image image : images) {
            imageRepository.delete(image);
        }

        // 게시물 삭제
        postRepository.delete(post);
    }

    @Transactional
    public PostDetailDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 조회수 1 증가
        post.setPostView(post.getPostView() + 1);
        postRepository.save(post);

        List<String> imageUrls = imageRepository.findByPost(post).stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        List<CommentDto> comments = post.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getUser().getNickname(),
                        comment.getCommentContent(),
                        LocalDateTime.parse(comment.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME)
                ))
                .collect(Collectors.toList());

        return new PostDetailDto(
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
    public List<PostListDto> getPostList(String sportsName, String boardName, String tagName, String sortMethod, String searchType, String searchContent, int page) {
        List<Post> posts = postRepository.findAll().stream()
                .filter(post -> post.getSports().getSportsName().equals(sportsName))
                .filter(post -> post.getBoard().getBoardName().equals(boardName))
                .filter(post -> post.getTag().getTagName().equals(tagName))
                .collect(Collectors.toList());

        if (searchContent != null && !searchContent.isEmpty()) {
            switch (searchType) {
                case "제목":
                    posts = posts.stream()
                            .filter(post -> post.getPostTitle().contains(searchContent))
                            .collect(Collectors.toList());
                    break;
                case "내용":
                    posts = posts.stream()
                            .filter(post -> post.getPostContent().contains(searchContent))
                            .collect(Collectors.toList());
                    break;
                case "작성자":
                    posts = posts.stream()
                            .filter(post -> post.getUser().getNickname().contains(searchContent))
                            .collect(Collectors.toList());
                    break;
            }
        }

        switch (sortMethod) {
            case "최신순":
                posts = posts.stream()
                        .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                        .collect(Collectors.toList());
                break;
            case "조회순":
                posts = posts.stream()
                        .sorted(Comparator.comparing(Post::getPostView).reversed())
                        .collect(Collectors.toList());
                break;
            case "댓글순":
                posts = posts.stream()
                        .sorted(Comparator.comparing(post -> post.getComments().size(), Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                break;
        }

        int start = (page - 1) * 14;
        int end = Math.min(start + 14, posts.size());
        posts = posts.subList(start, end);

        return posts.stream()
                .map(post -> new PostListDto(
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
