package com.likelion.scul.board.dto;

import org.antlr.v4.runtime.misc.NotNull;

public record CommentUpdateRequestDto(
        Long commentId,
        String commentContent,
        String createdAt
) {}
