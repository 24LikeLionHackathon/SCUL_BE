package com.likelion.scul.mypage.dto;

import java.util.List;

public class ActivityLikesDto {
    private List<LikedPostInfoDto> postsList;
    private int totalPosts;
    private int totalComments;
    private int totalLikes;
    private int participatingClubs;

    public ActivityLikesDto(List<LikedPostInfoDto> postsList, int totalPosts, int totalComments, int totalLikes, int participatingClubs) {
        this.postsList = postsList;
        this.totalPosts = totalPosts;
        this.totalComments = totalComments;
        this.totalLikes = totalLikes;
        this.participatingClubs = participatingClubs;
    }

    // Getters and Setters

    public List<LikedPostInfoDto> getPostsList() {
        return postsList;
    }

    public void setPostsList(List<LikedPostInfoDto> postsList) {
        this.postsList = postsList;
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


