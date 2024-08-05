package com.likelion.scul.mypage.controller;

import com.likelion.scul.mypage.dto.*;
import com.likelion.scul.mypage.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/header/{userNickname}")
    public ResponseEntity<MyPageHeaderDto> getHeader(@PathVariable String userNickname, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();

        MyPageHeaderDto headerInfo = myPageService.getHeaderInfo(userNickname,email);

        return ResponseEntity.ok(headerInfo);
    }

    @GetMapping("/activity/posts")
    public ResponseEntity<ActivityPostsDto> getActivityPosts(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ){
        ActivityPostsDto activityPostsInfo = myPageService.getActivityPostsInfo(userNickname, page);
        return ResponseEntity.ok(activityPostsInfo);
    }

    @GetMapping("/activity/comments")
    public ResponseEntity<ActivityCommentsDto> getActivityComments(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ){
        ActivityCommentsDto activityCommentsInfo = myPageService.getActivityComments(userNickname, page);
        return ResponseEntity.ok(activityCommentsInfo);
    }

    @GetMapping("/activity/clubs")
    public ResponseEntity<ActivityClubsDto> getActivityClubs(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ) {
        ActivityClubsDto activityClubsInfo = myPageService.getActivityClubsInfo(userNickname, page);
        return ResponseEntity.ok(activityClubsInfo);
    }

    @GetMapping("/activity/likes")
    public ResponseEntity<ActivityLikesDto> getActivityLikes(
            @RequestParam("page") int page,
            @RequestParam("userNickname") String userNickname
    ){
        ActivityLikesDto likedPostsDto=myPageService.getActivityLikes(userNickname, page);
        return ResponseEntity.ok(likedPostsDto);
    }
}
