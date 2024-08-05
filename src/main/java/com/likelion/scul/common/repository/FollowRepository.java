package com.likelion.scul.common.repository;

import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    int countByFollower(User user);
    int countByFollowed(User user);

    // 특정 사용자가 다른 사용자를 팔로우하고 있는지 확인
    boolean existsByFollowerAndFollowed(User follower, User followed);

    public List<Follow> findByfollowerUserId(Long userId); // 팔로우 하는 유저의 아이디가 1인 것

    public List<Follow> findByfollowedUserId(Long userId); // 팔로우 당하는 유저의 아이디가 1인 것

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followed.userId = :userId")
    int countFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.userId = :userId")
    int countFollowingByUserId(@Param("userId") Long userId);
}
