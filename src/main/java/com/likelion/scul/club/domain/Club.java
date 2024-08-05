package com.likelion.scul.club.domain;

import com.likelion.scul.club.TimeEntity;
import com.likelion.scul.club.dto.ClubRequest;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
@Entity
public class Club extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;
    private String clubName;
    private String clubContent;
    private String clubPlace;
    private LocalDate clubDate;
    private LocalDate clubEndDate;

    private int clubTotalNumber;
    private int clubParticipateNumber;

    private String clubQnaLink;
    private String clubParticipateLink;

    private int clubCost;
    private String clubStatus;
    private String clubImage;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sports_id")
    private Sports sports;

    @OneToMany(mappedBy = "club")
    private List<ClubUser> clubUsers;

    public Club(Long clubId, String clubName, String clubContent, String clubPlace, LocalDate clubDate, LocalDate clubEndDate,
                int clubTotalNumber, int clubParticipateNumber, String clubQnaLink, String clubParticipateLink,
                int clubCost, String clubStatus, String clubImage, User user, Sports sports) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.clubContent = clubContent;
        this.clubPlace = clubPlace;
        this.clubDate = clubDate;
        this.clubEndDate = clubEndDate;
        this.clubTotalNumber = clubTotalNumber;
        this.clubParticipateNumber = clubParticipateNumber;
        this.clubQnaLink = clubQnaLink;
        this.clubParticipateLink = clubParticipateLink;
        this.clubCost = clubCost;
        this.clubStatus = clubStatus;
        this.clubImage = clubImage;
        this.user = user;
        this.sports = sports;
    }

    public Club(ClubRequest clubRequest, String clubStatus, User user, Sports sports, String clubImage, int participateNumber) {
        this.clubName = clubRequest.getClubName();
        this.clubContent = clubRequest.getClubContent();
        this.clubPlace = clubRequest.getClubPlace();
        this.clubDate = clubRequest.getClubDate();
        this.clubEndDate = clubRequest.getClubEndDate();
        this.clubTotalNumber = clubRequest.getClubTotalNumber();
        this.clubParticipateNumber = participateNumber;
        this.clubQnaLink = clubRequest.getClubQnaLink();
        this.clubParticipateLink = clubRequest.getClubParticipateLink();
        this.clubCost = clubRequest.getClubCost();
        this.clubImage = clubImage;
        this.clubStatus = clubStatus;
        this.user = user;
        this.sports = sports;
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
