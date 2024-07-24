package com.likelion.scul.auth.config;

import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public WebConfig(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtService, userService))
                .addPathPatterns("/api/**"); // 필요한 경로에만 적용
    }
}
