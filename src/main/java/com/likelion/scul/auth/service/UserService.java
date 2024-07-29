package com.likelion.scul.auth.service;

import com.likelion.scul.auth.domain.RefreshToken;
import com.likelion.scul.auth.domain.dto.AddUserInfoRequest;
import com.likelion.scul.auth.repository.RefreshTokenRepository;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
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

    public User makeNewUser(AddUserInfoRequest request,
                            HttpSession session) {

        String email = (String) session.getAttribute("UserEmail");

        // 새로운 사용자 등록
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(request.getName());
        newUser.setGender(request.getGender());
        newUser.setAge(request.getAge());
        newUser.setRegion(request.getRegion());
        newUser.setNickname(request.getNickname());

        return newUser;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public RefreshToken createAndSaveRefreshToken(User user, String token) {
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
