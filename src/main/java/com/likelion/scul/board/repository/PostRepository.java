package com.likelion.scul.board.repository;

import com.likelion.scul.board.domain.Post;
import com.likelion.scul.common.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);
    int countByUser(User user);
}
