package com.likelion.scul.auth.config;

import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;
    private final UserService userService;

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/api/auth/**",
            "/follow",// 로그인 및 회원가입 경로
            "/refresh-token",
            "/api/auth/**", // 로그인 및 회원가입 경로
            "/oauth2/**", // OAuth2 관련 경로
            "/initial" // 추가 정보 입력 페이// 추가 정보 입력 처리
    };

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    public WebConfig(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtService, userService))
                .addPathPatterns("/**")
                .excludePathPatterns(AUTH_WHITELIST);
    }
}
