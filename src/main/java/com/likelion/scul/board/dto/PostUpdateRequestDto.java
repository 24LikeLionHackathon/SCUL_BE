package com.likelion.scul.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDto{
    private Long postId;
    private String boardName;
    private String tagName;
    private String sportsName;
    private String postTitle;
    private String postContent;
    private String createdAt;
    private List<MultipartFile> files;
}
