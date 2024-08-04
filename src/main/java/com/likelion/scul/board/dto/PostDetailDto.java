package com.likelion.scul.board.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailDto(
        Long postId,
        String nickname,
        String boardName,
        String tagName,
        String postTitle,
        String postContent,
        LocalDateTime createdAt,
        int postView,
        List<String> imageUrls,
        int likeCount,
        List<CommentDto> comments,
        Boolean isMe
) {}
