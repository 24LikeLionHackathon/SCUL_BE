package com.likelion.scul.board.service;

import com.likelion.scul.board.domain.Comment;
import com.likelion.scul.board.domain.Post;
import com.likelion.scul.board.dto.CommentDto;
import com.likelion.scul.board.dto.CommentRequestDto;
import com.likelion.scul.board.dto.CommentUpdateRequestDto;
import com.likelion.scul.board.repository.CommentRepository;
import com.likelion.scul.board.repository.PostRepository;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentDto createComment(String email, CommentRequestDto commentRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(commentRequestDto.postId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        LocalDateTime createdDateTime = LocalDateTime.parse(commentRequestDto.createdAt(), DateTimeFormatter.ISO_DATE_TIME);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setCommentContent(commentRequestDto.commentContent());
        comment.setCreatedAt(createdDateTime);
        commentRepository.save(comment);

        // Retrieve user image URL
        String userImageUrl = user.getUserImage() != null ? user.getUserImage().getImageUrl() : null;

        return new CommentDto(comment.getCommentId(), user.getNickname(), comment.getCommentContent(), createdDateTime, userImageUrl,comment.getUser().getEmail().equals(email));
    }

    @Transactional
    public CommentDto updateComment(Long postId,String email, CommentUpdateRequestDto commentUpdateRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("User not authorized to update this comment");
        }

        LocalDateTime createdDateTime = LocalDateTime.parse(commentUpdateRequestDto.createdAt(), DateTimeFormatter.ISO_DATE_TIME);

        comment.setCommentContent(commentUpdateRequestDto.commentContent());
        comment.setCreatedAt(createdDateTime);

        // Retrieve user image URL
        String userImageUrl = user.getUserImage() != null ? user.getUserImage().getImageUrl() : null;

        return new CommentDto(comment.getCommentId(), user.getNickname(), comment.getCommentContent(), createdDateTime, userImageUrl,comment.getUser().getEmail().equals(email));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        commentRepository.delete(comment);
    }
}
