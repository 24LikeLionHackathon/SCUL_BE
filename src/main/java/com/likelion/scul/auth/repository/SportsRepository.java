package com.likelion.scul.auth.repository;

import com.likelion.scul.auth.domain.Sports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportsRepository extends JpaRepository<Sports, Long> {
    Optional<Sports> findBySportsName(String sportsName);
}