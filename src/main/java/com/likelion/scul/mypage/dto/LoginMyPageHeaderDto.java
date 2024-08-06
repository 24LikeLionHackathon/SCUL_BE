package com.likelion.scul.mypage.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMyPageHeaderDto {

    private String nickname;
    private String topPrioritySportsName;
    private int followerNum;
    private int followedNum;
    private int participatingClubNum;
    private String userProfileImageUrl;
}
