package com.likelion.scul.board.dto;

import javax.swing.text.StyledEditorKit;
import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String nickname,
        String commentContent,
        LocalDateTime createdAt,
        String userImageUrl,
        Boolean isMe
) {
}
