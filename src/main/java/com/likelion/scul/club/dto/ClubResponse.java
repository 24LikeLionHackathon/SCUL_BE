package com.likelion.scul.club.dto;

import com.likelion.scul.club.Club;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ClubResponse {
    private Long clubId;
    private String clubName;
    private String clubContent;
    private String clubPlace;
    private LocalDate clubDate;

    private int clubTotalNumber;
    private int clubParticipateNumber;

    private String clubQnaLink;
    private String clubParticipateLink;

    private int clubCost;
    private String clubStatus;
    private String clubImage;

    private String userName;

    public ClubResponse(Long clubId, String clubName, String clubContent, String clubPlace, LocalDate clubDate, int clubTotalNumber, int clubParticipateNumber, String clubQnaLink, String clubParticipateLink, int clubCost, String clubStatus, String clubImage, String name) {
    }

    public static ClubResponse toClubResponse(Club club) {
        return new ClubResponse(
                club.getClubId(), club.getClubName(), club.getClubContent(), club.getClubPlace(), club.getClubDate(),
                club.getClubTotalNumber(), club.getClubParticipateNumber(), club.getClubQnaLink(), club.getClubParticipateLink(),
                club.getClubCost(), club.getClubStatus(), club.getClubImage(), club.getUser().getName()
        );
    }
}
