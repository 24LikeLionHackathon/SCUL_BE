package com.likelion.scul.auth.controller;

import com.likelion.scul.auth.domain.KakaoToken;
import com.likelion.scul.auth.domain.dto.AddUserInfoRequest;
import com.likelion.scul.auth.google.GoogleRequest;
import com.likelion.scul.auth.google.GoogleResponse;
import com.likelion.scul.auth.google.GoogleUserInfoResponse;
import com.likelion.scul.auth.service.GoogleService;
import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.KakaoService;
import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.service.UserSportsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Value("${kakao.client.key}")
    private String kakaoClientKey;
    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;
    @Value("${kakao.auth.url}")
    private String kakaoAuthUrl;
    @Value("${kakao.user.api.url}")
    private String kakaoUserApiUrl;

    private final JwtService jwtService;
    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final UserSportsService userSportsService;

    @Autowired
    private RestTemplate restTemplate;

    public LoginController(
            JwtService jwtService,
            UserService userService,
            KakaoService kakaoService,
            GoogleService googleService,
            UserSportsService userSportsService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.kakaoService = kakaoService;
        this.googleService = googleService;
        this.userSportsService = userSportsService;
    }

    @GetMapping("/oauth2/google")
    public ResponseEntity<Map<String, Object>> loginGoogle(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) throws IOException {
        // Request accessToken, refreshToken from Google
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri(googleRedirectUri)
                .grantType("authorization_code")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);

        String jwtToken = resultEntity.getBody().getId_token();
        String accessToken = resultEntity.getBody().getAccess_token();

        // Request user email from Google
        ResponseEntity<GoogleUserInfoResponse> userInfoResponseEntity = restTemplate.getForEntity(
                "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken,
                GoogleUserInfoResponse.class);

        GoogleUserInfoResponse userInfo = userInfoResponseEntity.getBody();
        String email = userInfo.getEmail();

        // Check if the email exists in the database
        Optional<User> user = userService.findByEmail(email);
        Map<String, Object> responseBody = new HashMap<>();
        if (!user.isPresent()) {
            session.setAttribute("loginType", "google");
            session.setAttribute("googleUser", userInfo);
            session.setAttribute("UserEmail", email);
            responseBody.put("is_member", false);
        } else {
            responseBody.put("access_token", "Bearer " + jwtService.createAccessToken(email));
            responseBody.put("refresh_token", "Bearer " + jwtService.findOrCreateRefreshToken(user.get()));
            responseBody.put("is_member", true);
            responseBody.put("userId", user.get().getUserId()); // Add userId
            responseBody.put("userNickname", user.get().getNickname());

        }
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<Map<String, Object>> loginKakao(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) throws IOException {
        // Get Token from Kakao Auth Server
        KakaoToken kakaoToken = kakaoService.getToken(authCode);
        String accessToken = kakaoToken.getAccess_token();
        // Get user's KakaoAccount Email information
        String email = kakaoService.getEmail(accessToken);

        Optional<User> user = userService.findByEmail(email);
        Map<String, Object> responseBody = new HashMap<>();
        if (!user.isPresent()) {
            session.setAttribute("loginType", "kakao");
            session.setAttribute("UserEmail", email);
            responseBody.put("is_member", false);
        } else {
            responseBody.put("access_token", "Bearer " + jwtService.createAccessToken(email));
            responseBody.put("refresh_token", "Bearer " + jwtService.findOrCreateRefreshToken(user.get()));
            responseBody.put("is_member", true);
            responseBody.put("userId", user.get().getUserId()); // Add userId
            responseBody.put("userNickname", user.get().getNickname());

        }
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/auth/join/submit-info")
    public ResponseEntity<Map<String, String>> submitAdditionalInfo(
            @RequestBody AddUserInfoRequest request,
            HttpSession session) {

        User newUser;
        if ("kakao".equals(session.getAttribute("loginType"))) {
            newUser = userService.makeNewUser(request, session);
            newUser = userService.saveUser(newUser);
            // Save new UserSports to DB
            userSportsService.saveUserSports(request.getSportsName(), newUser);
        } else {
            newUser = userService.makeNewUser(request, session);
            newUser = userService.saveUser(newUser);

            for (String sport : request.getSportsName()) {
                System.out.println("sport = " + sport);
            }
            // Save new UserSports to DB
            userSportsService.saveUserSports(request.getSportsName(), newUser);
        }

        // Issue tokens based on user's email
        String accessJwt = jwtService.createAccessToken(newUser.getEmail());
        String refreshJwt = jwtService.createRefreshToken(newUser.getEmail());

        // Create and save Refresh Token
        jwtService.createAndSaveRefreshToken(newUser, refreshJwt);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", "Bearer " + accessJwt);
        tokens.put("refresh_token", "Bearer " + refreshJwt);
        tokens.put("userId", newUser.getUserId().toString()); // Add userId to response
        tokens.put("userNickname", newUser.getNickname());

        return ResponseEntity.ok(tokens);
    }

}