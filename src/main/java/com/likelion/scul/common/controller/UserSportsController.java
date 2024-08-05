package com.likelion.scul.common.controller;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.usersports.UserSportsRequest;
import com.likelion.scul.common.dto.usersports.UserSportsResponse;
import com.likelion.scul.common.service.UserSportsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sports")
public class UserSportsController {
    private UserSportsService userSportsService;
    private UserService userService;

    public UserSportsController(UserSportsService userSportsService, UserService userService) {
        this.userSportsService = userSportsService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> modifyUserSports(@RequestBody UserSportsRequest userSportsRequest, HttpServletRequest servletRequest) {
        User user = userService.extractUserByAccessToken(servletRequest);
        userSportsService.deleteUserSports(user);
        userSportsService.saveUserSports(userSportsRequest.getSportsName(), user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserSportsResponse> getUserSports(HttpServletRequest request) {
        Long userId = userService.extractUserIdByAccessToken(request);
        UserSportsResponse response = userSportsService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
