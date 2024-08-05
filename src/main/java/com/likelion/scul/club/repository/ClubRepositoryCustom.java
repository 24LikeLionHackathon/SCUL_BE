package com.likelion.scul.club.repository;

import com.likelion.scul.club.domain.Club;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClubRepositoryCustom {

    List<Club> findBySearchOption(Long sportsId, String status, LocalDate date, String place, int minCost, int maxCost, int participantCount, String searchCondition, String SearchText);
}
