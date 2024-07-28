package com.likelion.scul.common.controller;

import com.likelion.scul.common.domain.Follow;
import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
