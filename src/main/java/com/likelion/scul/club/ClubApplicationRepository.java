package com.likelion.scul.club;

import com.likelion.scul.club.domain.Club;
import com.likelion.scul.club.domain.ClubApplication;
import com.likelion.scul.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {
    ClubApplication findByClubAndApplicant(Club club, User applicant);
}
