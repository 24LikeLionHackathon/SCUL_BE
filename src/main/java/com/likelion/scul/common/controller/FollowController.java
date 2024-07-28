package com.likelion.scul.common.controller;

import com.likelion.scul.common.dto.follow.FollowRequest;
import com.likelion.scul.common.service.FollowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {

    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public void addFollow(@RequestBody FollowRequest request) {
        followService.saveFollow(request);

    }
}
