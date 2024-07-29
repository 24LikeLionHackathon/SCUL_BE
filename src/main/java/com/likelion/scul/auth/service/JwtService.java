package com.likelion.scul.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1시간
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
        return "Bearer " + token;
    }

    public String createRefreshToken(String email) {
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        System.out.println("Generated Refresh Token: " + token);
        return "Bearer " + token;
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

    public Claims getClaimsFromToken(String token) {
        token = removeBearerPrefix(token);
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        return claimsJws.getBody();
    }

    private String removeBearerPrefix(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
