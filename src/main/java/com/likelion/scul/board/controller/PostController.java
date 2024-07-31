package com.likelion.scul.board.controller;

import com.likelion.scul.board.dto.PostDetailDto;
import com.likelion.scul.board.dto.PostListDto;
import com.likelion.scul.board.dto.PostRequestDto;
import com.likelion.scul.board.dto.PostUpdateRequestDto;
import com.likelion.scul.board.dto.PostDeleteRequestDto;
import com.likelion.scul.board.dto.PostListRequestDto;
import com.likelion.scul.board.service.PostService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/newpost")
    public ResponseEntity<String> createPost(@ModelAttribute PostRequestDto postRequestDto, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            System.out.println(postRequestDto.getPostTitle());
            postService.createPost(postRequestDto, email);
            return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatepost")
    public ResponseEntity<String> updatePost(@ModelAttribute PostUpdateRequestDto postUpdateRequestDto, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.updatePost(postUpdateRequestDto, email);
            return new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts")
    public ResponseEntity<String> deletePost(@RequestBody PostDeleteRequestDto postDeleteRequestDto, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.deletePost(postDeleteRequestDto.postId(), email);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{post_id}")
    public ResponseEntity<PostDetailDto> getPostDetail(@PathVariable Long post_id) {
        try {
            PostDetailDto postDetail = postService.getPostDetail(post_id);
            return new ResponseEntity<>(postDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/posts/list")
    public ResponseEntity<List<PostListDto>> getPostList(@RequestBody PostListRequestDto postListRequestDto) {
        try {
            List<PostListDto> postList = postService.getPostList(postListRequestDto);
            return new ResponseEntity<>(postList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/recent-posts")
    public ResponseEntity<List<PostListDto>> getRecentPosts() {
        try {
            List<PostListDto> recentPosts = postService.getRecentPosts();
            return new ResponseEntity<>(recentPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/hot-posts")
    public ResponseEntity<List<PostListDto>> getHotPosts() {
        try {
            List<PostListDto> hotPosts = postService.getHotPosts();
            return new ResponseEntity<>(hotPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
