package com.likelion.scul.club.repository;

import com.likelion.scul.club.domain.Club;
import com.likelion.scul.club.domain.ClubUser;
import com.likelion.scul.common.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Page<ClubUser> findClubsByUser(User user, Pageable pageable);
    Club findByClubId(Long id);

    List<Club> findAllBySports_SportsIdOrderByCreatedAtDesc(Long sportsId);

    List<Club> findByClubIdIn(List<Long> clubIds);

    int countByUser(User user);
}
