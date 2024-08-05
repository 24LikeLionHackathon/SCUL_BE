package com.likelion.scul.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikedPostInfoDto {
    private String postImage;
    private String sportsName;
    private String boardName;
    private String postTitle;
    private String postAuthorNickname;
    private LocalDateTime createdAt;
    private int commentCount;

    public LikedPostInfoDto(String postImage, String sportsName, String boardName, String postTitle, String postAuthorNickname, LocalDateTime createdAt, int commentCount) {
        this.postImage = postImage;
        this.sportsName = sportsName;
        this.boardName = boardName;
        this.postTitle = postTitle;
        this.postAuthorNickname = postAuthorNickname;
        this.createdAt = createdAt;
        this.commentCount = commentCount;
    }
}

