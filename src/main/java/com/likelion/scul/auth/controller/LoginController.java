package com.likelion.scul.auth.controller;

import com.likelion.scul.auth.domain.GoogleRefreshToken;
import com.likelion.scul.auth.domain.KakaoRefreshToken;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
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

//    @GetMapping("/")
//    public String home(){
//        return "index";
//    }

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

    @GetMapping("/oauth2/kakao/redirect")
    public RedirectView redirectKakaoLogin() {
        StringBuilder reqUrl = new StringBuilder("https://kauth.kakao.com/oauth/authorize");
        reqUrl.append("?client_id=").append(kakaoClientKey)
                .append("&redirect_uri=").append(kakaoRedirectUri)
                .append("&response_type=code")
                .append("&scope=account_email");
        return new RedirectView(reqUrl.toString());
    }

    @GetMapping("/oauth2/google")
    public ResponseEntity<Map<String, String>> loginGoogle(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) throws IOException {
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

        ResponseEntity<GoogleUserInfoResponse> userInfoResponseEntity = restTemplate.getForEntity(
                "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken,
                GoogleUserInfoResponse.class);

        GoogleUserInfoResponse userInfo = userInfoResponseEntity.getBody();
        String email = userInfo.getEmail();

        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            session.setAttribute("loginType", "google");
            session.setAttribute("GoogleUser", resultEntity.getBody());
            session.setAttribute("UserEmail", email);
            try {
                response.sendRedirect("/additional-info");
                return null;
            } catch (IOException e) {
                throw new IllegalStateException("리다이렉트에 실패했습니다.");
            }
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtService.createAccessToken(email));
        tokens.put("refresh_token", jwtService.findByUser(user.get()));
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<Map<String, String>> loginKakao(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) {
        KakaoToken kakaoToken = kakaoService.getToken(authCode);
        String accessToken = kakaoToken.getAccess_token();
        String email = kakaoService.getEmail(accessToken);

        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            session.setAttribute("loginType", "kakao");
            session.setAttribute("KakaoUser", kakaoToken);
            session.setAttribute("UserEmail", email);
            try {
                response.sendRedirect("/additional-info");
                return null;
            } catch (IOException e) {
                throw new IllegalStateException("리다이렉트에 실패했습니다.");
            }
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtService.createAccessToken(email));
        tokens.put("refresh_token", jwtService.findByUser(user.get()));
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/additional-info")
    public String additionalInfoForm() {
        return "additional-info";
    }

    @PostMapping("/auth/join/submit-info")
    public ResponseEntity<Map<String, String>> submitAdditionalInfo(
            @RequestBody AddUserInfoRequest request,
            HttpSession session) {

        if ("kakao".equals(session.getAttribute("loginType"))) {
            User newUser = userService.makeNewUser(request, session);
            newUser = userService.saveUser(newUser);
            KakaoToken kakaoToken = (KakaoToken) session.getAttribute("KakaoUser");
            KakaoRefreshToken kakaoRefreshToken = kakaoService.makeKakaoRefreshToken(newUser, kakaoToken.getAccess_token());
            kakaoService.saveKakaoRefreshToken(kakaoRefreshToken);
            userSportsService.saveUserSports(request.getSportsName(), newUser);

            String accessJwt = jwtService.createAccessToken(newUser.getEmail());
            String refreshJwt = jwtService.createRefreshToken(newUser.getEmail());
            jwtService.createAndSaveRefreshToken(newUser, refreshJwt);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessJwt);
            tokens.put("refresh_token", refreshJwt);

            return ResponseEntity.ok(tokens);
        } else {
            GoogleResponse googleToken = (GoogleResponse) session.getAttribute("GoogleUser");
            if (googleToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            User newUser = userService.makeNewUser(request, session);
            newUser = userService.saveUser(newUser);
            GoogleRefreshToken googleRefreshToken = googleService.makeGoogleRefreshToken(newUser, googleToken.getRefresh_token());
            googleService.saveGoogleRefreshToken(googleRefreshToken);
            userSportsService.saveUserSports(request.getSportsName(), newUser);

            String accessJwt = jwtService.createAccessToken(newUser.getEmail());
            String refreshJwt = jwtService.createRefreshToken(newUser.getEmail());
            jwtService.createAndSaveRefreshToken(newUser, refreshJwt);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessJwt);
            tokens.put("refresh_token", refreshJwt);

            return ResponseEntity.ok(tokens);
        }
    }
}
