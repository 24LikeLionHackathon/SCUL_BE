package com.likelion.scul.common.dto.follow;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FollowProfileResponse {
    Long userId;
    String userImageUrl;
    String nickName;
    String favoriteSports;
    int followingNumber;
    int followerNumber;
}