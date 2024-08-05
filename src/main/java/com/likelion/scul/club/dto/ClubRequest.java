package com.likelion.scul.club.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubRequest {
    private String clubName;
    private String clubContent;
    private String clubPlace;
    private LocalDate clubDate;
    private LocalDate clubEndDate;
    private int clubTotalNumber;
    private String clubQnaLink;
    private String clubParticipateLink;
    private int clubCost;
    private MultipartFile clubImage;
    private String sportsName;
}
