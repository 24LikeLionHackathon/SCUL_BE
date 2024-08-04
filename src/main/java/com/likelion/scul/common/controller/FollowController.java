package com.likelion.scul.common.controller;

import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.dto.follow.FollowNumResponse;
import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.dto.follow.FollowResponse;
import com.likelion.scul.common.dto.follow.FollowProfileResponse;
import com.likelion.scul.common.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<Follow> addFollow(@RequestBody FollowRequest request) {
        Follow follow = followService.saveFollow(request);
        return ResponseEntity.ok(follow);
    }

    @GetMapping
    public ResponseEntity<FollowResponse> getFollow(@RequestParam Long userId) {
        FollowResponse response = followService.getFollows(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFollow(@RequestParam Long followId) {
        followService.deleteFollow(followId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/num")
    public ResponseEntity<FollowNumResponse> getFollowNum(@RequestParam Long userId) {
        FollowNumResponse response = followService.getFollowNum(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/follower")
    public ResponseEntity<List<FollowProfileResponse>> getFollowers(@RequestParam int pageNum , HttpServletRequest request) {
//        Claims claims = (Claims) request.getAttribute("claims");
//        String email = claims.getSubject();
        // 테스트 용
        String email = "hkjbrian@gmail.com";
        // 테스트 용
        return ResponseEntity.ok(followService.getFollowers(email, pageNum));
    }
    @GetMapping("/following")
    public ResponseEntity<List<FollowProfileResponse>> getFollowings(@RequestParam int pageNum , HttpServletRequest request) {
//        Claims claims = (Claims) request.getAttribute("claims");
//        String email = claims.getSubject();
        // 테스트 용
        String email = "hkjbrian@gmail.com";
        // 테스트 용
        return ResponseEntity.ok(followService.getFollowings(email, pageNum));
    }
}
