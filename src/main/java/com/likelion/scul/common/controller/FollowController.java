package com.likelion.scul.common.controller;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.follow.*;
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
    public ResponseEntity addFollow(@RequestBody FollowRequest followRequest, HttpServletRequest servletRequest) {
        User user = userService.extractUserByAccessToken(servletRequest);
        Follow follow = followService.saveFollow(followRequest, user);
        return ResponseEntity.ok("성공적으로 follow 했습니다");
    }

//    @GetMapping
//    public ResponseEntity<FollowResponse> getFollow(HttpServletRequest request) {
//        User user = userService.extractUserByAccessToken(request);
//        FollowResponse response = followService.getFollows(user.getUserId());
//        return ResponseEntity.ok(response);
//    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFollow(@RequestBody FollowRequest followRequest, HttpServletRequest servletRequest) {
        Long userId = userService.extractUserIdByAccessToken(servletRequest);

        User followedUser = userService.findByNickName(followRequest.getFollowedNickName())
                .orElseThrow(()->new IllegalStateException("user not found"));
        followService.deleteFollow(userId, followedUser.getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/follower")
    public ResponseEntity<List<FollowProfileResponse>> getFollowers(@RequestBody MyPageFollowRequest request) {
        String email = userService.findByNickName(request.getUserNickName())
                .orElseThrow(()-> new IllegalStateException("닉네임으로 해당 유저를 찾을 수 없습니다."))
                .getEmail();

        return ResponseEntity.ok(followService.getFollowers(email, request.getPageNum()));
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowProfileResponse>> getFollowings(@RequestBody MyPageFollowRequest request) {
        String email = userService.findByNickName(request.getUserNickName())
                .orElseThrow(()-> new IllegalStateException("닉네임으로 해당 유저를 찾을 수 없습니다."))
                .getEmail();

        return ResponseEntity.ok(followService.getFollowings(email, request.getPageNum()));
    }

    @GetMapping("/num")
    public ResponseEntity<FollowNumResponse> getFollowNum(@RequestBody FollowRequest request) {
        Long userId = userService.findByNickName(request.getFollowedNickName())
                .orElseThrow(()-> new IllegalStateException("닉네임으로 해당 유저를 찾을 수 없습니다."))
                .getUserId();

        FollowNumResponse response = followService.getFollowNum(userId);
        return ResponseEntity.ok(response);
    }
}