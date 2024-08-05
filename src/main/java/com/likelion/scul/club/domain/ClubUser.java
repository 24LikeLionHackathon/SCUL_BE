package com.likelion.scul.club.domain;

import com.likelion.scul.common.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ClubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ClubUserId;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public ClubUser(Club club, User user) {
        this.club = club;
        this.user = user;
    }

    public ClubUser() {

    }
}

