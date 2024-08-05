package com.likelion.scul.club.dto;

import com.likelion.scul.club.domain.ClubApplication;
import lombok.Getter;

@Getter
public class ClubApplicationResponse {
    private long clubApplicationId;
    private String clubName;
    private String applicantNickname;
    private String leaderNickname;
    private String applicantIntro;
    private Boolean isApprove;

    ClubApplicationResponse(long clubApplicationId, String clubName, String applicantNickname, String leaderNickname, String applicantIntro, Boolean isApprove) {
        this.clubApplicationId = clubApplicationId;
        this.clubName = clubName;
        this.applicantNickname = applicantNickname;
        this.leaderNickname = leaderNickname;
        this.applicantIntro = applicantIntro;
        this.isApprove = isApprove;
    }

    public static ClubApplicationResponse toClubApplicationResponse(ClubApplication clubApplication) {
        System.out.println("id: " + clubApplication.getClubApplicationId());
        System.out.println("club: " + clubApplication.getClub().getClubName());
        System.out.println("applicant: " + clubApplication.getApplicant().getName());
        System.out.println("leader: " + clubApplication.getLeader().getName());
        System.out.println("applicantIntro: " + clubApplication.getApplicantIntro());

        return new ClubApplicationResponse(
                clubApplication.getClubApplicationId(), clubApplication.getClub().getClubName(), clubApplication.getApplicant().getNickname(),
                clubApplication.getLeader().getNickname(), clubApplication.getApplicantIntro(), clubApplication.getIsApprove()
        );
    }
}
