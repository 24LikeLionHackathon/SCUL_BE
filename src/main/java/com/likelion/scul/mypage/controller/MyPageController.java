package com.likelion.scul.mypage.controller;

import com.likelion.scul.mypage.dto.ActivityCommentsDto;
import com.likelion.scul.mypage.dto.ActivityLikesDto;
import com.likelion.scul.mypage.dto.ActivityPostsDto;
import com.likelion.scul.mypage.dto.MyPageHeaderDto;
import com.likelion.scul.mypage.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/header")
    public ResponseEntity<MyPageHeaderDto> getHeader(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();

        MyPageHeaderDto headerInfo = myPageService.getHeaderInfo(email);

        return ResponseEntity.ok(headerInfo);
    }

    @GetMapping("/activity/posts")
    public ResponseEntity<ActivityPostsDto> getActivityPosts(
            HttpServletRequest request,
            @RequestParam("page") int page) {

        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();

        ActivityPostsDto activityPostsInfo = myPageService.getActivityPostsInfo(email, page);
        return ResponseEntity.ok(activityPostsInfo);
    }

    @GetMapping("/activity/comments")
    public ResponseEntity<ActivityCommentsDto> getActivityComments(
            HttpServletRequest request,
            @RequestParam("page") int page) {

        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();

        ActivityCommentsDto activityCommentsInfo = myPageService.getActivityComments(email, page);
        return ResponseEntity.ok(activityCommentsInfo);
    }

    @GetMapping("/activity/likes")
    public ResponseEntity<ActivityLikesDto> getActivityLikes(
            HttpServletRequest request,
            @RequestParam("page") int page) {

        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();

        ActivityLikesDto activityLikesInfo = myPageService.getActivityLikes(email, page);
        return ResponseEntity.ok(activityLikesInfo);
    }
}
