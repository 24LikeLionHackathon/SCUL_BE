package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClubInfoDto {
    private String sportsName;
    private String clubName;
    private String clubPlace;
    private String userNickname;
    private LocalDate clubDate;
    private long postId;
}