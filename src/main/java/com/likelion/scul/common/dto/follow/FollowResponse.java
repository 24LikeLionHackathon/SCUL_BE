package com.likelion.scul.common.dto.follow;

import com.likelion.scul.common.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class FollowResponse {
    private List<User> followers ;
    private List<User> followings ;
}
