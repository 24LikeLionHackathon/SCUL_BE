package com.likelion.scul.common.repository;

import com.likelion.scul.common.domain.UserSports;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSportsRepository extends JpaRepository<UserSports, Long> {

    List<UserSports> findByUserUserId(Long userId);

    @Query(value = "SELECT sports_name " +
            "FROM user_sports us " +
            "JOIN sports s ON us.sports_id = s.sports_id " +
            "WHERE us.user_id = :userId " +
            "ORDER BY us.sports_priority; ", nativeQuery = true)
    List<String> findSportsNamesByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM User_Sports us WHERE us.user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") Long userId);
}
