package com.likelion.scul.board.domain;

import com.likelion.scul.auth.domain.Sports;
import com.likelion.scul.auth.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "sports_id")
    private Sports sports;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    private String postTitle;
    private String postContent;
    private LocalDateTime createdAt;
    private int postView;

    public Post(Board board, Tag tag, Sports sports, User user, String postTitle, String postContent, LocalDateTime createdAt, int postView) {
        this.board = board;
        this.tag = tag;
        this.sports = sports;
        this.user = user;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.createdAt = createdAt;
        this.postView = postView;
    }

    public Post() {

    }

    // Getters
}
