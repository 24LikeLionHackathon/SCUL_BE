package com.likelion.scul.common.controller;

import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.dto.usersports.UserSportsRequest;
import com.likelion.scul.common.dto.usersports.UserSportsResponse;
import com.likelion.scul.common.service.UserSportsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<Void> modifyUserSports(@RequestBody UserSportsRequest request) {
        User user = userService.findById(request.getUserId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저의 id입니다."));
        userSportsService.deleteUserSports(user);
        userSportsService.saveUserSports(request.getSportsName(), user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserSportsResponse> getUserSports(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String email = claims.getSubject();
        Optional<User> user = userService.findByEmail(email);
        UserSportsResponse response = userSportsService.findByUserId(user.get().getUserId());
        return ResponseEntity.ok(response);
    }
}
