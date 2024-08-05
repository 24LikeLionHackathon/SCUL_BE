package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActivityClubsDto {
    private List<ClubInfoDto> clubs;
    private int totalPosts;
    private int totalComments;
    private int totalParticipatedClubs;
    private int totalLikes;
}