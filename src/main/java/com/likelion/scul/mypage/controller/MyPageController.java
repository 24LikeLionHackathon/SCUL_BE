package com.likelion.scul.mypage.controller;

import com.likelion.scul.mypage.dto.*;
import com.likelion.scul.mypage.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/api/header")
    public ResponseEntity<MyPageHeaderDto> getHeader(
            @RequestParam("userId") Long userId,
            @RequestParam("userNickname") String userNickname) {


        MyPageHeaderDto headerInfo = myPageService.getHeaderInfo(userNickname,userId);

        return ResponseEntity.ok(headerInfo);
    }

    @GetMapping("/mypage/header")
    public ResponseEntity<LoginMyPageHeaderDto> getHeader(
            HttpServletRequest request
    ) {
        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();
        LoginMyPageHeaderDto headerInfo = myPageService.getMyHeaderInfo(email);

        return ResponseEntity.ok(headerInfo);
    }

    @GetMapping("/mypage/activity/posts")
    public ResponseEntity<ActivityPostsDto> getActivityPosts(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ){
        ActivityPostsDto activityPostsInfo = myPageService.getActivityPostsInfo(userNickname, page);
        return ResponseEntity.ok(activityPostsInfo);
    }

    @GetMapping("/mypage/activity/comments")
    public ResponseEntity<ActivityCommentsDto> getActivityComments(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ){
        ActivityCommentsDto activityCommentsInfo = myPageService.getActivityComments(userNickname, page);
        return ResponseEntity.ok(activityCommentsInfo);
    }

    @GetMapping("/mypage/activity/clubs")
    public ResponseEntity<ActivityClubsDto> getActivityClubs(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ) {
        ActivityClubsDto activityClubsInfo = myPageService.getActivityClubsInfo(userNickname, page);
        return ResponseEntity.ok(activityClubsInfo);
    }

    @GetMapping("/mypage/activity/likes")
    public ResponseEntity<ActivityLikesDto> getActivityLikes(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ){
        ActivityLikesDto likedPostsDto=myPageService.getActivityLikes(userNickname, page);
        return ResponseEntity.ok(likedPostsDto);
    }
}
