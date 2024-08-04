package com.likelion.scul.club.repository;

import com.likelion.scul.club.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club findByClubId(Long id);

    List<Club> findAllBySports_SportsIdOrderByCreatedAtDesc(Long sportsId);
}
