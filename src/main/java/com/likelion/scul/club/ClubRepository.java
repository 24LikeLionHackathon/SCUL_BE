package com.likelion.scul.club;

import com.likelion.scul.common.domain.Sports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club findByClubId(Long id);

    List<Club> findAllBySports_SportsIdOrderByCreatedAtDesc(Long sportsId);
}
