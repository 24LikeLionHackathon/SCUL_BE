package com.likelion.scul.board.dto;

public record PostListRequestDto(
        String sportsName,
        String boardName,
        String tagName,
        String sortMethod,
        String searchType,
        String searchContent,
        int page
) {}
