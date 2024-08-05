package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPostsDto {
    private List<PostInfoDto> postsList;
    private int totalPosts;
    private int totalComments;
    private int totalLikes;
    private int participatingClubs;

}
