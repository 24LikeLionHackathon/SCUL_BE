package com.likelion.scul.common.domain;

import jakarta.persistence.*;

@Entity
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

    // Getters and Setters

    public Long getUserSportId() {
        return userSportId;
    }

    public void setUserSportId(Long userSportId) {
        this.userSportId = userSportId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sports getSports() {
        return sports;
    }

    public void setSports(Sports sports) {
        this.sports = sports;
    }
}
