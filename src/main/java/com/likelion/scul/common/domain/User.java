package com.likelion.scul.common.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Transient
    private String name;
    private String gender;
    private int age;
    private String email;
    private String nickname;
    @OneToMany(mappedBy = "user")
    private List<UserSports> userSports;

    @OneToOne(mappedBy = "user")
    private UserImage userImage;

}
