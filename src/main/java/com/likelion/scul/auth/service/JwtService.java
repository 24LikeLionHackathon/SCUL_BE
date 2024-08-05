package com.likelion.scul.auth.service;

import com.likelion.scul.auth.domain.RefreshToken;
import com.likelion.scul.auth.repository.RefreshTokenRepository;
import com.likelion.scul.common.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private RefreshTokenRepository refreshTokenRepository;

    public JwtService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60*24*7; // 임시로 7일
    public static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7일

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(String email) {
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        System.out.println("Generated Access Token: " + token);
        return token;
    }

    public String createRefreshToken(String email) {
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        System.out.println("Generated Refresh Token: " + token);
        return token;
    }

    public RefreshToken createAndSaveRefreshToken(User user, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + JwtService.REFRESH_TOKEN_VALIDITY));

        return refreshTokenRepository.save(refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            token = removeBearerPrefix(token);
            //지워
            System.out.println("Validating token: " + token);
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            //지워
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    private String removeBearerPrefix(String token) {
            if (token.startsWith("Bearer ")) {
                return token.substring(7).trim(); // 공백 제거
            }
            return token.trim(); // 공백 제거
    }

    public Claims getClaimsFromToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public String findOrCreateRefreshToken(User user) {
        // Refresh token 조회
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByUser(user);

        if (tokenOptional.isPresent()) {
            RefreshToken refreshToken = tokenOptional.get();
            // 만료 여부 확인
            if (!isTokenExpired(refreshToken.getExpiryDate())) {
                // 만료되지 않은 경우 기존 토큰 반환
                return refreshToken.getToken();
            }
            // 만료된 경우 기존 토큰 삭제
            deleteRefreshToken(user);
        }

        // 새로 발급하여 저장
        String newRefreshToken = createRefreshToken(user.getEmail());
        createAndSaveRefreshToken(user, newRefreshToken);
        return newRefreshToken;
    }

    private boolean isTokenExpired(Date expiryDate) {
        return expiryDate.before(new Date());
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
