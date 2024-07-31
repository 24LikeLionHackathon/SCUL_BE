package com.likelion.scul.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class KakaoRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long kakaoRefreshTokenId;
    private String kakaoRefreshToken;
    private long userId;

    public long getKakaoRefreshTokenId() {
        return kakaoRefreshTokenId;
    }

    public void setKakaoRefreshTokenId(long kakaoRefreshTokenId) {
        this.kakaoRefreshTokenId = kakaoRefreshTokenId;
    }

    public String getKakaoRefreshToken() {
        return kakaoRefreshToken;
    }

    public void setKakaoRefreshToken(String kakaoRefreshToken) {
        this.kakaoRefreshToken = kakaoRefreshToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
