package com.likelion.scul.common.dto.follow;

import lombok.Data;

@Data
public class FollowRequest {
    private Long followerUserId;
    private Long followedUserId;
}
