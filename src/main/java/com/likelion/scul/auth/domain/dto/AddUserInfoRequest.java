package com.likelion.scul.auth.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddUserInfoRequest {
    private String name;
    private String gender;
    private int age;
    private String nickname;
    private List<String> sportsName;
}
