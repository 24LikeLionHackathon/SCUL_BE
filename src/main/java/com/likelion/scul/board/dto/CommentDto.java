package com.likelion.scul.board.dto;

import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String nickname,
        String commentContent,
        LocalDateTime createdAt
) {}
