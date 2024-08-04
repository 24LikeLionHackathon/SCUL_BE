package com.likelion.scul.auth.controller;

import com.likelion.scul.auth.domain.RefreshToken;
import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String refreshToken = authHeader.substring(7);

        try {
            // Refresh Token의 유효성 검사
            if (!jwtService.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Refresh Token에서 사용자 정보 추출
            Claims claims = jwtService.getClaimsFromToken(refreshToken);
            String email = claims.getSubject();

            // 데이터베이스에서 Refresh Token 확인
            Optional<User> userOpt = userService.findByEmail(email);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            User user = userOpt.get();
            Optional<RefreshToken> refreshTokenOpt = jwtService.findByToken(refreshToken);
            if (!refreshTokenOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // 새로운 Access Token 발급
            String newAccessToken = jwtService.createAccessToken(email);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", "Bearer " + newAccessToken); // Bearer 접두사를 붙임

            return ResponseEntity.ok(tokens);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token has expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred"));
        }
    }
}
