package com.likelion.scul.auth.repository;

import com.likelion.scul.auth.domain.KakaoRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoRefreshTokenRepository extends JpaRepository<KakaoRefreshToken, Long> {
    public Optional<KakaoRefreshToken> findByUserId(Long userId);
}
