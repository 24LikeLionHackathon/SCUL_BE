package com.likelion.scul.board.dto;

import java.time.LocalDateTime;

public record CommentDto(
        String nickname,
        String commentContent,
        LocalDateTime createdAt
) {}
