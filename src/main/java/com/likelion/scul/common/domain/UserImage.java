package com.likelion.scul.common.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
