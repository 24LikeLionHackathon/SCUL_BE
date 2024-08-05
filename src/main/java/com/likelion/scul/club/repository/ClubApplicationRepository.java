package com.likelion.scul.club.repository;

import com.likelion.scul.club.domain.Club;
import com.likelion.scul.club.domain.ClubApplication;
import com.likelion.scul.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {

    ClubApplication findByClubApplicationId(Long id);
    ClubApplication findByClubAndApplicant(Club club, User applicant);

}
