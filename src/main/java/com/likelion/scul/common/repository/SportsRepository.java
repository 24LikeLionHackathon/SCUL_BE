package com.likelion.scul.common.repository;

import com.likelion.scul.common.domain.Sports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportsRepository extends JpaRepository<Sports, Long> {
    Optional<Sports> findByName(String sportsName);
}
