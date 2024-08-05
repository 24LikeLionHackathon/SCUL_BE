package com.likelion.scul.auth.config;

import com.likelion.scul.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class OptionalJwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public OptionalJwtInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.validateToken(token)) {
                    Claims claims = jwtService.getClaimsFromToken(token);
                    request.setAttribute("email", claims.getSubject());
                }
            } catch (ExpiredJwtException e) {
                // 토큰이 만료되었을 경우 별도의 처리 가능
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access token has expired");
                return false;
            }
        }

        // 토큰이 없거나 유효하지 않더라도 진행 (비회원 처리)
        return true;
    }
}
