package com.likelion.scul.board.controller;

import com.likelion.scul.board.service.LikeService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;


    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<String> addLike(@RequestParam("post_id") Long postId, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            likeService.addLike(email, postId);
            return new ResponseEntity<>("Like added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> removeLike(@RequestParam("post_id") Long postId, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            likeService.removeLike(email, postId);
            return new ResponseEntity<>("Like removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
