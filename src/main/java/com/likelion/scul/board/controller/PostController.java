package com.likelion.scul.board.controller;

import com.likelion.scul.board.dto.*;
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

    @PutMapping("/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,@ModelAttribute PostUpdateRequestDto postUpdateRequestDto, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.updatePost(postId,postUpdateRequestDto, email);
            return new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            postService.deletePost(postId, email);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //여기에 interceptor가 동작하고 있는데 만약에 비회원 로그인이면 허용해줘야함
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailDto> getPostDetail(@PathVariable Long postId,HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            PostDetailDto postDetail = postService.getPostDetail(postId,email);
            return new ResponseEntity<>(postDetail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/postlist")
    public ResponseEntity<PostListResponseDto> getPostList(@RequestBody PostListRequestDto postListRequestDto) {
        try {
            PostListResponseDto postListResponse = postService.getPostList(postListRequestDto);
            return new ResponseEntity<>(postListResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/recent-posts")
    public ResponseEntity<List<PostListDto>> getRecentPosts() {
        try {
            List<PostListDto> recentPosts = postService.getRecentPosts();
            return new ResponseEntity<>(recentPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hot-posts")
    public ResponseEntity<List<PostListDto>> getHotPosts() {
        try {
            List<PostListDto> hotPosts = postService.getHotPosts();
            return new ResponseEntity<>(hotPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
