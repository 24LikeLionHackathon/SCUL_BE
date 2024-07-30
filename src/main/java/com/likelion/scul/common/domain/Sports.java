package com.likelion.scul.common.domain;

import com.likelion.scul.auth.domain.UserSports;
import com.likelion.scul.board.domain.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Entity
public class Sports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sportsId;

    private String sportsName;
    private String sportsType;

    @Transient
    private String sportsDefaultImg;

    @OneToMany(mappedBy = "sports")
    private List<UserSports> userSports;

    @OneToMany(mappedBy = "sports")
    private List<Board> boards;
}
