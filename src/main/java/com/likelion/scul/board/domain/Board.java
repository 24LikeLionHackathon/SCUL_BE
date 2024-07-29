package com.likelion.scul.board.domain;

import com.likelion.scul.auth.domain.Sports;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String boardName;

    @OneToMany(mappedBy = "board")
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name = "sports_id")
    private Sports sports;

    // Getters and Setters
}
