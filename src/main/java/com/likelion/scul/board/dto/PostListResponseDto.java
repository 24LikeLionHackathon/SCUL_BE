package com.likelion.scul.board.dto;

import java.util.List;

public class PostListResponseDto {
    private List<PostListDto> posts; // 페이징된 게시물 목록
    private int totalPosts; // 총 게시물 수

    public PostListResponseDto(List<PostListDto> paginatedPosts, int totalPosts) {
    }
}
