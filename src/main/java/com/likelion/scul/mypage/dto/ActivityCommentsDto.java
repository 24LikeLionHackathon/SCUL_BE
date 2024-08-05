package com.likelion.scul.mypage.dto;

import java.util.List;

public class ActivityCommentsDto {
    private List<CommentInfoDto> commentList;
    private int totalPosts;
    private int totalComments;
    private int totalLikes;
    private int participatingClubs;

    public ActivityCommentsDto(List<CommentInfoDto> commentList, int totalPosts, int totalComments, int totalLikes, int participatingClubs) {
        this.commentList = commentList;
        this.totalPosts = totalPosts;
        this.totalComments = totalComments;
        this.totalLikes = totalLikes;
        this.participatingClubs = participatingClubs;
    }

    // Getters and Setters

    public List<CommentInfoDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentInfoDto> commentList) {
        this.commentList = commentList;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getParticipatingClubs() {
        return participatingClubs;
    }

    public void setParticipatingClubs(int participatingClubs) {
        this.participatingClubs = participatingClubs;
    }
}
