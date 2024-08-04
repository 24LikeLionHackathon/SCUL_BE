package com.likelion.scul.board.repository;

import com.likelion.scul.board.domain.Comment;
import com.likelion.scul.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByUser(User user);
}
