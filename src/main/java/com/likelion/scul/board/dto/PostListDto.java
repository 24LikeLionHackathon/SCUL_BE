package com.likelion.scul.board.dto;

import java.time.LocalDateTime;

public record PostListDto(
        String nickname,
        String tagName,
        String postTitle,
        LocalDateTime createdAt,
        int likeCount,
        int postView,
        int commentCount,
        String imageUrl
) {}
