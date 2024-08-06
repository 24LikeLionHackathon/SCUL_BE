package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInfoDto {
    private String postImage;
    private String sportsName;
    private String boardName;
    private String postTitle;
    private LocalDateTime createdAt;
    private int commentCount;

    private long postId;
}
