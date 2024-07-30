package com.likelion.scul.club;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByClubId(Long id);
    List<Club> findAllBySportsId(Long sportsId);
}
