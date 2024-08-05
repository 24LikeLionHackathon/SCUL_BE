package com.likelion.scul.club.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class ClubRequest {
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
    private MultipartFile clubImage;
    private String sportsName;
}
