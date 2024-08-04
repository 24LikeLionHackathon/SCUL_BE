package com.likelion.scul.board.repository;

import com.likelion.scul.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardName(String boardName);

    Optional<Board> findByBoardNameAndSportsSportsName(String boardName, String sportsName);
}
