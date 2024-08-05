package com.likelion.scul.board.controller;

import com.likelion.scul.board.dto.CommentDto;
import com.likelion.scul.board.dto.CommentRequestDto;
import com.likelion.scul.board.dto.CommentUpdateRequestDto;
import com.likelion.scul.board.service.CommentService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                                    HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            CommentDto commentDto = commentService.createComment(email, commentRequestDto);
            return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,@RequestBody CommentUpdateRequestDto commentUpdateRequestDto,
                                                    HttpServletRequest request) {
        System.out.println("이까지 들어가나?");
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String email = claims.getSubject();
            CommentDto commentDto = commentService.updateComment(commentId,email, commentUpdateRequestDto);
            return new ResponseEntity<>(commentDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                HttpServletRequest request) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
