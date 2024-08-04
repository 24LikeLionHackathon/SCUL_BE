package com.likelion.scul.club.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ClubSearchRequest {
    private String clubStatus;
    private String clubPlace;
    private LocalDate clubDate;
    private int clubMinCost;
    private int clubMaxCost;
    private int participantsCount;

}
