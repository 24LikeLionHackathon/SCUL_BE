package com.likelion.scul.board.dto;

import java.util.List;

public record PostUpdateRequestDto(
        Long postId,
        String boardName,
        String tagName,
        String sportsName,
        String postTitle,
        String postContent,
        String createdAt,
        List<String> imageUrls
) {}
