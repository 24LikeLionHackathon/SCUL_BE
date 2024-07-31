package com.likelion.scul.auth.service;

import com.likelion.scul.auth.domain.GoogleRefreshToken;
import com.likelion.scul.auth.repository.GoogleRefreshTokenRepository;
import com.likelion.scul.common.domain.User;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {
    private GoogleRefreshTokenRepository googleRefreshTokenRepository;

    public GoogleService(GoogleRefreshTokenRepository googleRefreshTokenRepository) {
        this.googleRefreshTokenRepository = googleRefreshTokenRepository;
    }

    public void saveGoogleRefreshToken(GoogleRefreshToken googleRefreshToken) {
        googleRefreshTokenRepository.save(googleRefreshToken);
    }

    public GoogleRefreshToken makeGoogleRefreshToken(User user, String refreshToken) {
        GoogleRefreshToken googleRefreshToken = new GoogleRefreshToken();
        googleRefreshToken.setGoogleRefreshToken("Bearer " + refreshToken);
        googleRefreshToken.setUserId(user.getUserId());

        return googleRefreshToken;
    }

}
