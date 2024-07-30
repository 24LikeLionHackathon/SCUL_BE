package com.likelion.scul.board.domain;

import com.likelion.scul.common.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String commentContent;
    private String createdAt;

    // Getters and Setters

    public Comment(User user, Post post, String commentContent, String createdAt) {
        this.user = user;
        this.post = post;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
    }

    public Comment() {

    }
}
