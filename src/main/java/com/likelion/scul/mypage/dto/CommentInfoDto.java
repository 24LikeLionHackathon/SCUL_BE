package com.likelion.scul.mypage.dto;

import java.time.LocalDateTime;

public class CommentInfoDto {
    private String commentContent;
    private LocalDateTime createdAt;
    private String sportsName;
    private String boardName;
    private String postTitle;

    public CommentInfoDto(String commentContent, LocalDateTime createdAt, String sportsName, String boardName, String postTitle) {
        this.commentContent = commentContent;
        this.createdAt = createdAt;
        this.sportsName = sportsName;
        this.boardName = boardName;
        this.postTitle = postTitle;
    }

    // Getters and Setters

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}

