package com.likelion.scul.board.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private String boardName;
    private String tagName;
    private String sportsName;
    private String postTitle;
    private String postContent;
    private String createdAt;
    private List<MultipartFile> files=new ArrayList<>();;

}
