package com.likelion.scul.board.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

public record CommentUpdateRequestDto(
        String commentContent,
        String createdAt
) {}
