package com.likelion.scul.common.domain;

import jakarta.persistence.*;

@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User follower;  // 팔로우 하는 유저

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User followed;  // 팔로우 당하는 유저
}
