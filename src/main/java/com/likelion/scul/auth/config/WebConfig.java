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

    // 로그인이 필요한 경로 설정
    private static final String[] PROTECTED_PATHS = {
            //예시
//            "/user/**",         // 회원 전용 페이지
            "/newpost",
            "/dashboard/**",    // 대시보드
            "/profile/**",      // 프로필 페이지
            // 필요한 경로 (게시물 작성/삭제/수정, 댓글작성/삭제/수정, 마이페이지 ..)
            // 프론트는 우리가 매번 보내주는 is_authorized를 보고 페이지를 어떻게 보여줄지 정한다
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
        // PROTECTED_PATHS 경로에만 인터셉터 적용
        registry.addInterceptor(new JwtInterceptor(jwtService, userService))
                .addPathPatterns(PROTECTED_PATHS);
    }
}
