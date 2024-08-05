package com.likelion.scul.club.dto;

import com.likelion.scul.club.domain.Club;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ClubResponse {
    private Long clubId;
    private String clubName;
    private String clubContent;
    private String clubPlace;
    private LocalDate clubDate;
    private LocalDate clubEndDate;
    private LocalDateTime createdAt;

    private int clubTotalNumber;
    private int clubParticipateNumber;

    private String clubQnaLink;
    private String clubParticipateLink;

    private int clubCost;
    private String clubStatus;
    private String clubImage;

    private String userName;
    private String sportsName;

    public ClubResponse(Long clubId, String clubName, String clubContent, String clubPlace, LocalDate clubDate, LocalDate clubEndDate, LocalDateTime createdAt, int clubTotalNumber, int clubParticipateNumber, String clubQnaLink, String clubParticipateLink, int clubCost, String clubStatus, String clubImage, String name, String sportsName) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubContent = clubContent;
        this.clubPlace = clubPlace;
        this.clubDate = clubDate;
        this.clubEndDate = clubEndDate;
        this.createdAt = createdAt;
        this.clubTotalNumber = clubTotalNumber;
        this.clubParticipateNumber = clubParticipateNumber;
        this.clubQnaLink = clubQnaLink;
        this.clubParticipateLink = clubParticipateLink;
        this.clubCost = clubCost;
        this.clubStatus = clubStatus;
        this.clubImage = clubImage;
        this.userName = name;
        this.sportsName = sportsName;
    }

    public static ClubResponse toClubResponse(Club club) {
        return new ClubResponse(
                club.getClubId(), club.getClubName(), club.getClubContent(), club.getClubPlace(), club.getClubDate(), club.getClubEndDate(), club.getCreatedAt(),
                club.getClubTotalNumber(), club.getClubParticipateNumber(), club.getClubQnaLink(), club.getClubParticipateLink(),
                club.getClubCost(), club.getClubStatus(), club.getClubImage(), club.getUser().getName(), club.getSports().getSportsName()
        );
    }
}
