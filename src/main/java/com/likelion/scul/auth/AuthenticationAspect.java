package com.likelion.scul.auth;

import com.likelion.scul.auth.config.JwtInterceptor;
import com.likelion.scul.auth.service.JwtService;
import com.likelion.scul.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Aspect
@Component
public class AuthenticationAspect {

    private final JwtInterceptor jwtInterceptor;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Autowired
    public AuthenticationAspect(JwtInterceptor jwtInterceptor, HttpServletRequest request, HttpServletResponse response) {
        this.jwtInterceptor = jwtInterceptor;
        this.request = request;
        this.response = response;
    }

    @Pointcut("@annotation(authenticated)")
    public void authenticatedMethods(Authenticated authenticated) {}

    @Before("authenticatedMethods(authenticated)")
    public void checkAuthentication(Authenticated authenticated) throws Exception {
        if (!jwtInterceptor.preHandle(request, response, null)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            throw new SecurityException("Unauthorized");
        }
    }
}
