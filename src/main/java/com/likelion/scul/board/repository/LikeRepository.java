package com.likelion.scul.board.repository;

import com.likelion.scul.board.domain.Like;
import com.likelion.scul.board.domain.Post;
import com.likelion.scul.common.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);

    Page<Like> findByUser(User user, Pageable pageable);

    int countByUser(User user);
}
