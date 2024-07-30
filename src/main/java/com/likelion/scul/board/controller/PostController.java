package com.likelion.scul.board.controller;

import com.likelion.scul.board.dto.PostDetailDto;
import com.likelion.scul.board.dto.PostListDto;
import com.likelion.scul.board.service.PostService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 게시판 작성
     * */
    @PostMapping("/newpost")
    public ResponseEntity<String> createPost(@RequestParam("board_name") String boardName,
                                             @RequestParam("tag_name") String tagName,
                                             @RequestParam("sports_name") String sportsName,
                                             @RequestParam("post_title") String postTitle,
                                             @RequestParam("post_content") String postContent,
                                             @RequestParam("created_at") String createdAt,
                                             @RequestParam("image_url") List<String> imageUrls,
                                             HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.createPost(boardName, tagName, sportsName, postTitle, postContent, createdAt, imageUrls, email);
            return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 게시판 수정
     * */
    @PutMapping("/updatepost/{post_id}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
                                             @RequestParam("board_name") String boardName,
                                             @RequestParam("tag_name") String tagName,
                                             @RequestParam("sports_name") String sportsName,
                                             @RequestParam("post_title") String postTitle,
                                             @RequestParam("post_content") String postContent,
                                             @RequestParam("created_at") String createdAt,
                                             @RequestParam("image_url") List<String> imageUrls,
                                             HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.updatePost(postId, boardName, tagName, sportsName, postTitle, postContent, createdAt, imageUrls, email);
            return new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable Long post_id, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.deletePost(post_id, email);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 게시물 상세보기
     * */
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostDetailDto> getPostDetail(@PathVariable Long post_id) {
        try {
            PostDetailDto postDetail = postService.getPostDetail(post_id);
            return new ResponseEntity<>(postDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 게시물 목록
     * */
    @GetMapping("/posts")
    public ResponseEntity<List<PostListDto>> getPostList(@RequestParam String sports_name,
                                                         @RequestParam String board_name,
                                                         @RequestParam String tag_name,
                                                         @RequestParam String sort_method,
                                                         @RequestParam String search_type,
                                                         @RequestParam String search_content,
                                                         @RequestParam int page) {
        try {
            List<PostListDto> postList = postService.getPostList(sports_name, board_name, tag_name, sort_method, search_type, search_content, page);
            return new ResponseEntity<>(postList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
