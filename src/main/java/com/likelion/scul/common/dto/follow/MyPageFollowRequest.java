package com.likelion.scul.common.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyPageFollowRequest {
    String userNickName;
    int pageNum;
}