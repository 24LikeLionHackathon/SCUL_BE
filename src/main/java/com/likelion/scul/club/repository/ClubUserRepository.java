package com.likelion.scul.club.repository;

import com.likelion.scul.club.domain.ClubUser;
import com.likelion.scul.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {
    @Query("SELECT cu.club.clubId FROM ClubUser cu WHERE cu.user = :user")
    List<Long> findClubIdsByUser(@Param("user") User user);
}
