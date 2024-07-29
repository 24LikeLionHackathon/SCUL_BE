package com.likelion.scul.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<Post> posts;

    // Getters and Setters
}
