package com.likelion.scul.club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByClubId(Long id);

    //@Query("SELECT * FROM club where sports_id = ?")
    List<Club> findAllBySports_SportsId(Long sportsId);
}
