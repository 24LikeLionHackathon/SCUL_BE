package com.likelion.scul.common.dto.follow;

import lombok.Data;

@Data
public class FollowRequest {
    private String followerNickName; // 팔로우 하는 사람의 닉네임
    private String followedNickName; // 팔로우 당하는 사람의 닉네임
}
