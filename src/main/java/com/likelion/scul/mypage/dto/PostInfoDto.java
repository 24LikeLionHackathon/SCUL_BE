package com.likelion.scul.mypage.dto;

import java.time.LocalDateTime;

public class PostInfoDto {
    private String postImage;
    private String sportsName;
    private String boardName;
    private String postTitle;
    private LocalDateTime createdAt;
    private int commentCount;

    public PostInfoDto(String postImage, String sportsName, String boardName, String postTitle, LocalDateTime createdAt, int commentCount) {
        this.postImage = postImage;
        this.sportsName = sportsName;
        this.boardName = boardName;
        this.postTitle = postTitle;
        this.createdAt = createdAt;
        this.commentCount = commentCount;
    }

    // Getters and Setters

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
