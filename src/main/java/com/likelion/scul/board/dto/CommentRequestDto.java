package com.likelion.scul.board.dto;

public record CommentRequestDto(
        Long postId,
        String commentContent,
        String createdAt
) {}
