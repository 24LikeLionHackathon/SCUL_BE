package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInfoDto {
    private String commentContent;
    private LocalDateTime createdAt;
    private String sportsName;
    private String boardName;
    private String postTitle;
    private long postId;
}

