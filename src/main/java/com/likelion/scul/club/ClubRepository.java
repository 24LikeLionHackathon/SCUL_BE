package com.likelion.scul.club;

import com.likelion.scul.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByClubId(Long id);

    int countByUser(User user);
}
