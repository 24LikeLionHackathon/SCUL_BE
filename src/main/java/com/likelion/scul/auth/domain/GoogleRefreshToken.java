package com.likelion.scul.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GoogleRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long googleRefreshTokenId;
    private String googleRefreshToken;
    private long userId;

    public long getGoogleRefreshTokenId() {
        return googleRefreshTokenId;
    }

    public void setGoogleRefreshTokenId(long googleRefreshTokenId) {
        this.googleRefreshTokenId = googleRefreshTokenId;
    }

    public String getGoogleRefreshToken() {
        return googleRefreshToken;
    }

    public void setGoogleRefreshToken(String googleRefreshToken) {
        this.googleRefreshToken = googleRefreshToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
