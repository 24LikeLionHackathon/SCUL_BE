package com.likelion.scul.club.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubSearchRequest {
    private String clubStatus;
    private String clubPlace;
    private LocalDate clubDate;
    private int clubMinCost;
    private int clubMaxCost;
    private int totalMinCount;
    private int totalMaxCount;

    private String searchCondition;
    private String searchText;
}
