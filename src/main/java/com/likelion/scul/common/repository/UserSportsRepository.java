package com.likelion.scul.common.repository;

import com.likelion.scul.common.domain.UserSports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSportsRepository extends JpaRepository<UserSports, Long> {
}
