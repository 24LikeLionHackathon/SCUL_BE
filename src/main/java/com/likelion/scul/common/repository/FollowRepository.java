package com.likelion.scul.common.repository;

import com.likelion.scul.common.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    public List<Follow> findByfollowerUserId(Long userId); // 팔로우 하는 유저의 아이디가 1인 것
    public List<Follow> findByfollowedUserId(Long userId); // 팔로우 당하는 유저의 아이디가 1인 것
}
