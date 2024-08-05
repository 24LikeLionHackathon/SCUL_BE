package com.likelion.scul.club;

import com.likelion.scul.club.dto.ClubRequest;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sports_id")
    private Sports sports;

    public Club(Long clubId, String clubName, String clubContent, String clubPlace, LocalDate clubDate,
                int clubTotalNumber, int clubParticipateNumber, String clubQnaLink, String clubParticipateLink,
                int clubCost, String clubStatus, String clubImage, User user) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubContent = clubContent;
        this.clubPlace = clubPlace;
        this.clubDate = clubDate;
        this.clubTotalNumber = clubTotalNumber;
        this.clubParticipateNumber = clubParticipateNumber;
        this.clubQnaLink = clubQnaLink;
        this.clubParticipateLink = clubParticipateLink;
        this.clubCost = clubCost;
        this.clubStatus = clubStatus;
        this.clubImage = clubImage;
        this.user = user;
    }

    public Club(ClubRequest clubRequest, String clubStatus) {
        this.clubName = clubRequest.getClubName();
        this.clubContent = clubRequest.getClubContent();
        this.clubPlace = clubRequest.getClubPlace();
        this.clubDate = clubRequest.getClubDate();
        this.clubTotalNumber = clubRequest.getClubTotalNumber();
        this.clubParticipateNumber = clubRequest.getClubParticipateNumber();
        this.clubCost = clubRequest.getClubCost();
        this.clubImage = clubRequest.getClubImage();
        this.clubStatus = clubStatus;
    }

    public Club() {}

    public void update(String clubName, String clubContent, String clubPlace, LocalDate clubDate, int clubTotalNumber, int clubParticipateNumber, String clubQnaLink, String clubParticipateLink, int clubCost, String clubImage, String clubStatus) {
        this.clubName = clubName;
        this.clubContent = clubContent;
        this.clubPlace = clubPlace;
        this.clubDate = clubDate;
        this.clubTotalNumber = clubTotalNumber;
        this.clubParticipateNumber = clubParticipateNumber;
        this.clubQnaLink = clubQnaLink;
        this.clubParticipateLink = clubParticipateLink;
        this.clubCost = clubCost;
        this.clubImage = clubImage;
        this.clubStatus = clubStatus;
    }

}
