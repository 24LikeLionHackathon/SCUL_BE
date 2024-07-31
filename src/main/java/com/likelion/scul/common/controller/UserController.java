package com.likelion.scul.common.controller;

import com.likelion.scul.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> isNickNameDuplicate(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.isNickNameDuplicate(nickname));
    }
}
