package com.likelion.scul.club.dto;

import com.likelion.scul.club.domain.ClubApplication;
import lombok.Getter;

@Getter
public class ClubApplicationResponse {
    private long clubApplicationId;
    private String clubName;
    private String applicantName;
    private String leaderName;
    private String applicantIntro;

    ClubApplicationResponse(long clubApplicationId, String clubName, String applicantName, String leaderName, String applicantIntro) {
        this.clubApplicationId = clubApplicationId;
        this.clubName = clubName;
        this.applicantName = applicantName;
        this.leaderName = leaderName;
        this.applicantIntro = applicantIntro;
    }

    public static ClubApplicationResponse toClubApplicationResponse(ClubApplication clubApplication) {
        System.out.println("id: " + clubApplication.getClubApplicationId());
        System.out.println("club: " + clubApplication.getClub().getClubName());
        System.out.println("applicant: " + clubApplication.getApplicant().getName());
        System.out.println("leader: " + clubApplication.getLeader().getName());
        System.out.println("applicantIntro: " + clubApplication.getApplicantIntro());

        return new ClubApplicationResponse(
                clubApplication.getClubApplicationId(), clubApplication.getClub().getClubName(), clubApplication.getApplicant().getName(),
                clubApplication.getLeader().getName(), clubApplication.getApplicantIntro()
        );
    }
}
