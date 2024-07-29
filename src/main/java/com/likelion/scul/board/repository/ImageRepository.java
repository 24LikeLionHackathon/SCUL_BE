package com.likelion.scul.board.repository;

import com.likelion.scul.board.domain.Image;
import com.likelion.scul.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPost(Post post);
}
