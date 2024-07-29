package com.likelion.scul.auth.service;

import com.likelion.scul.auth.domain.RefreshToken;
import com.likelion.scul.auth.repository.RefreshTokenRepository;
import com.likelion.scul.common.repository.UserRepository;
import com.likelion.scul.common.domain.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public RefreshToken createRefreshToken(User user, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + JwtService.REFRESH_TOKEN_VALIDITY));

        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<User> getUserFromToken(String token, JwtService jwtService) {
        Claims claims = jwtService.getClaimsFromToken(token);
        String email = claims.getSubject();
        return findByEmail(email);
    }
}
