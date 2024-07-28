package com.likelion.scul.auth.controller;

import com.likelion.scul.auth.domain.KakaoToken;
import com.likelion.scul.auth.google.GoogleRequest;
import com.likelion.scul.auth.google.GoogleResponse;
import com.likelion.scul.auth.google.GoogleUserInfoResponse;
import com.likelion.scul.auth.kakao.KakaoAccount;
import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.KakaoService;
import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private RestTemplate restTemplate;

    public LoginController(JwtService jwtService, UserService userService, KakaoService kakaoService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.kakaoService = kakaoService;
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
    public void loginGoogle(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) throws IOException {
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
        String name = userInfo.getName();
        // GoogleUserInfoResponse 클래스에는 gender 필드가 없으므로 제거
        // String gender = userInfo.getGender();
        String picture = userInfo.getPicture();

        // 이메일이 데이터베이스에 존재하는지 확인
        Optional<User> user = userService.findByEmail(email);
        if (!user.isPresent()) {
            // 세션에 구글 사용자 정보 저장
            session.setAttribute("googleUser", userInfo);

            // 추가 정보 입력 페이지로 리디렉션
            response.sendRedirect("/additional-info");
            return;
        }
    }

    @GetMapping("/oauth2/kakao")
    public String loginKakao(@RequestParam(value = "code") String authCode, HttpSession session, HttpServletResponse response) {
        // Kakao Auth Server로 부터 Token 발급
        KakaoToken kakaoToken = kakaoService.getToken(authCode);
        String accessToken = kakaoToken.getAccess_token();
        // Token으로 User의 KakaoAccount Email 정보
        String email = kakaoService.getEmail(accessToken);

        Optional<User> user = userService.findByEmail(email);
        // 기존 회원이 아니라면
        if (!user.isPresent()) {
            // 세션에 구글 사용자 정보 저장
            session.setAttribute("KakaoUser", kakaoToken);
            session.setAttribute("UserEmail", email);
            // 추가 정보 입력 페이지로 리디렉션
            try {
                response.sendRedirect("/additional-info");
                return null;
            } catch (IOException e) {
                throw new IllegalStateException("리다이렉트에 실패했습니다.");
            }
        }
        // 기존 회원이라면
        // 내부 로그인 accessToken을 발급한다.
        return jwtService.createAccessToken(email);
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

        KakaoToken kakaoToken = (KakaoToken) session.getAttribute("kakaoUser");
        if (kakaoToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 새로운 유저 DB에 저장
        User newUser = userService.makeNewUser(name, gender, age, region, nickname, session);
        userService.saveUser(newUser);

        // 유저의 email을 기반으로 페이지 내부 토큰 발급
        String accessJwt = jwtService.createAccessToken(newUser.getEmail());
        String refreshJwt = jwtService.createRefreshToken(newUser.getEmail());

        // Refresh Token 생성 및 저장
        userService.createAndSaveRefreshToken(newUser, refreshJwt);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessJwt);
        tokens.put("refresh_token", refreshJwt);

        return ResponseEntity.ok(tokens);
    }
}
