package com.likelion.scul.common.domain;

import com.likelion.scul.board.domain.Board;
import jakarta.persistence.*;
import java.util.List;

@Entity
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
