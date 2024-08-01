package com.likelion.scul.auth.controller;

import com.likelion.scul.auth.google.GoogleUserInfoResponse;
import com.likelion.scul.auth.google.GoogleRequest;
import com.likelion.scul.auth.google.GoogleResponse;
import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin("*")
public class LoginController {

    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.pw}")
    private String googleClientPw;
    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    private final JwtService jwtService;
    private final UserService userService;

    public LoginController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/oauth2/google/redirect")
    public RedirectView redirectGoogleLogin() {
        StringBuilder reqUrl = new StringBuilder("https://accounts.google.com/o/oauth2/v2/auth");
        reqUrl.append("?client_id=").append(googleClientId)
                .append("&redirect_uri=").append(googleRedirectUri)
                .append("&response_type=code")
                .append("&scope=email%20profile%20openid")
                .append("&access_type=offline");
        return new RedirectView(reqUrl.toString());
    }

    @GetMapping("/oauth2/google")

    public ResponseEntity<Map<String, Object>> loginGoogle(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri(googleRedirectUri)
                .grantType("authorization_code")
                .build();

        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);

        String jwtToken = resultEntity.getBody().getId_token();
        String accessToken = resultEntity.getBody().getAccess_token();

        ResponseEntity<GoogleUserInfoResponse> userInfoResponseEntity = restTemplate.getForEntity(
                "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken,
                GoogleUserInfoResponse.class);

        GoogleUserInfoResponse userInfo = userInfoResponseEntity.getBody();
        String email = userInfo.getEmail();
        String name = userInfo.getName();
        String picture = userInfo.getPicture();

        // Check if the email already exists in the database
        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            // Save Google user information in the session
            session.setAttribute("googleUser", userInfo);

            // Return JSON indicating that the user is not a member
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("is_member", false);
            response.sendRedirect("http://localhost:3000/initial");
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }

        String accessJwt = jwtService.createAccessToken(email);
        String refreshJwt = jwtService.createRefreshToken(email);

        // Save Refresh Token
        userService.createAndSaveRefreshToken(user.get(), refreshJwt);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", accessJwt);
        tokens.put("refresh_token", refreshJwt);
        tokens.put("is_member", true);

        response.sendRedirect("http://localhost:3000/home");
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/additional-info")
    public String additionalInfoForm() {
        return "additional-info";
    }

    @PostMapping("/submit-additional-info")
    public ResponseEntity<Map<String, String>> submitAdditionalInfo(
            @RequestParam String name,
            @RequestParam String gender,
            @RequestParam int age,
            @RequestParam String region,
            @RequestParam String nickname,
            HttpSession session) {

        GoogleUserInfoResponse googleUser = (GoogleUserInfoResponse) session.getAttribute("googleUser");
        if (googleUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = googleUser.getEmail();

        // 새로운 사용자 등록
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setGender(gender);
        newUser.setAge(age);
        newUser.setRegion(region);
        newUser.setNickname(nickname);

        userService.saveUser(newUser);

        String accessJwt = jwtService.createAccessToken(email);
        String refreshJwt = jwtService.createRefreshToken(email);

        // Refresh Token 저장
        userService.createAndSaveRefreshToken(newUser, refreshJwt);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessJwt);
        tokens.put("refresh_token", refreshJwt);

        return ResponseEntity.ok(tokens);
    }
}
