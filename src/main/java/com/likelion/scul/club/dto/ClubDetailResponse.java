package com.likelion.scul.club.dto;

import lombok.Data;


@Data
public class ClubDetailResponse {
    private ClubResponse clubResponse;
    private Boolean isMe;

    public ClubDetailResponse(ClubResponse clubResponse, Boolean isMe) {
        this.clubResponse = clubResponse;
        this.isMe = isMe;
    }
}
