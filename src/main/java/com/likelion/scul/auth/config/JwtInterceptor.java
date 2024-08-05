package com.likelion.scul.auth.config;

import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.UserService;
import com.likelion.scul.common.domain.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtInterceptor(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.validateToken(token)) {
                    Claims claims = jwtService.getClaimsFromToken(token);
                    request.setAttribute("claims", claims);

                    // request에 user 정보 추가
                    String email = claims.getSubject();
                    Optional<User> optionalUser = userService.findByEmail(email);
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        request.setAttribute("user", user);
                    }
                    else{
                        return false;
                    }
                    return true;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access token has expired");
                return false;
            }
        }

        // 로그인 페이지로 리다이렉트
//        response.sendRedirect("/login");
        return false;
    }
}
