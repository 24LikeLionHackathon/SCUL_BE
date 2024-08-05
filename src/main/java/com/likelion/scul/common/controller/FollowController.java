package com.likelion.scul.common.controller;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.follow.FollowNumResponse;
import com.likelion.scul.common.dto.follow.FollowProfileResponse;
import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.dto.follow.FollowResponse;
import com.likelion.scul.common.service.FollowService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.protocol.HTTP;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private FollowService followService;
    private UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Follow> addFollow(@RequestBody FollowRequest request) {
        Follow follow = followService.saveFollow(request);
        return ResponseEntity.ok(follow);
    }

    @GetMapping
    public ResponseEntity<FollowResponse> getFollow(HttpServletRequest request) {
        User user = userService.extractUserByAccessToken(request);
        FollowResponse response = followService.getFollows(user.getUserId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFollow(@RequestParam Long followId) {
        followService.deleteFollow(followId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/num")
    public ResponseEntity<FollowNumResponse> getFollowNum(HttpServletRequest request) {
        User user = userService.extractUserByAccessToken(request);
        FollowNumResponse response = followService.getFollowNum(user.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/follower")
    public ResponseEntity<List<FollowProfileResponse>> getFollowers(@RequestParam int pageNum, HttpServletRequest request) {
        String email = userService.extractUserEmailByAccessToken(request);
        return ResponseEntity.ok(followService.getFollowers(email, pageNum));
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowProfileResponse>> getFollowings(@RequestParam int pageNum, HttpServletRequest request) {
        String email = userService.extractUserEmailByAccessToken(request);
        return ResponseEntity.ok(followService.getFollowings(email, pageNum));
    }
}
