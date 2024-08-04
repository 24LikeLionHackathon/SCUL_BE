package com.likelion.scul.common.domain;

import com.likelion.scul.board.domain.Board;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Data
public class Sports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sportsId;

    private String sportsName;

    @Transient
    private String sportsIcon;

    @Transient
    private String sportsDefaultImg;

    @OneToMany(mappedBy = "sports")
    private List<UserSports> userSports;

    @OneToMany(mappedBy = "sports")
    private List<Board> boards;

    // Getters and Setters
}
