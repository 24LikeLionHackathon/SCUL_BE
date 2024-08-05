package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLikesDto {
    private List<LikedPostInfoDto> postsList;
    private int totalPosts;
    private int totalComments;
    private int totalLikes;
    private int participatingClubs;

    public ActivityLikesDto(List<LikedPostInfoDto> postsList, int totalPosts, int totalComments, int totalLikes, int participatingClubs) {
        this.postsList = postsList;
        this.totalPosts = totalPosts;
        this.totalComments = totalComments;
        this.totalLikes = totalLikes;
        this.participatingClubs = participatingClubs;
    }

}


