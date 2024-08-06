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
public class FollowController {

    private FollowService followService;
    private UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @PostMapping("/follow")
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

    @DeleteMapping("/follow")
    public ResponseEntity<Void> deleteFollow(@RequestBody FollowRequest followRequest, HttpServletRequest servletRequest) {
        Long userId = userService.extractUserIdByAccessToken(servletRequest);

        User followedUser = userService.findByNickName(followRequest.getFollowedNickName())
                .orElseThrow(()->new IllegalStateException("user not found"));
        followService.deleteFollow(userId, followedUser.getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/follower")
    public ResponseEntity<List<FollowProfileResponse>> getFollowers(
            @RequestParam("userNickname") String userNickname,
            @RequestParam("pageNum") int pageNum
    ) {
        MyPageFollowRequest request = new MyPageFollowRequest(userNickname, pageNum);
        String email = userService.findByNickName(request.getUserNickName())
                .orElseThrow(()-> new IllegalStateException("닉네임으로 해당 유저를 찾을 수 없습니다."))
                .getEmail();

        return ResponseEntity.ok(followService.getFollowers(email, request.getPageNum()));
    }

    @GetMapping("/api/following")
    public ResponseEntity<List<FollowProfileResponse>> getFollowings(
            @RequestParam("userNickname") String userNickname,
            @RequestParam("pageNum") int pageNum
    ) {
        MyPageFollowRequest request = new MyPageFollowRequest(userNickname, pageNum);
        String email = userService.findByNickName(request.getUserNickName())
                .orElseThrow(()-> new IllegalStateException("닉네임으로 해당 유저를 찾을 수 없습니다."))
                .getEmail();

        return ResponseEntity.ok(followService.getFollowings(email, request.getPageNum()));
    }

    @GetMapping("/api/num")
    public ResponseEntity<FollowNumResponse> getFollowNum(@RequestBody FollowRequest request) {
        Long userId = userService.findByNickName(request.getFollowedNickName())
                .orElseThrow(()-> new IllegalStateException("닉네임으로 해당 유저를 찾을 수 없습니다."))
                .getUserId();

        FollowNumResponse response = followService.getFollowNum(userId);
        return ResponseEntity.ok(response);
    }
}