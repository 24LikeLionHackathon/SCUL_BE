package com.likelion.scul.mypage.dto;

public class MyPageHeaderDto {

    private String nickname;
    private String topPrioritySportsName;
    private int followerNum;
    private int followedNum;
    private int participatingClubNum;
    private String userProfileImageUrl;

    private Boolean isMe;

    public MyPageHeaderDto(String nickname, String topPrioritySportsName, int followerNum, int followedNum, int participatingClubNum, String userProfileImageUrl,Boolean isMe) {
        this.nickname = nickname;
        this.topPrioritySportsName = topPrioritySportsName;
        this.followerNum = followerNum;
        this.followedNum = followedNum;
        this.participatingClubNum = participatingClubNum;
        this.userProfileImageUrl = userProfileImageUrl;
        this.isMe=isMe;
    }

    // Getters and Setters

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTopPrioritySportsName() {
        return topPrioritySportsName;
    }

    public void setTopPrioritySportsName(String topPrioritySportsName) {
        this.topPrioritySportsName = topPrioritySportsName;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public int getFollowedNum() {
        return followedNum;
    }

    public void setFollowedNum(int followedNum) {
        this.followedNum = followedNum;
    }

    public int getParticipatingClubNum() {
        return participatingClubNum;
    }

    public void setParticipatingClubNum(int participatingClubNum) {
        this.participatingClubNum = participatingClubNum;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }
}
