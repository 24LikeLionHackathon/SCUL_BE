package com.likelion.scul.common.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserSports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSportId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sports_id")
    private Sports sports;

    private int sportsPriority;
}
