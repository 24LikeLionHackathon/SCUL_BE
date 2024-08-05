package com.likelion.scul.auth.repository;

import com.likelion.scul.auth.domain.GoogleRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleRefreshTokenRepository extends JpaRepository<GoogleRefreshToken, Long> {
    public Optional<GoogleRefreshToken> findByUserId(Long userId);
}
