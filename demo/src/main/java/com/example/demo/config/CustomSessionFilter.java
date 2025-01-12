package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomSessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 쿠키에서 SESSIONID 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    // 세션 유효성 검증
                    if (isValidSession(sessionId)) {
                        // 인증 정보 설정
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                sessionId, null, null
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidSession(String sessionId) {
        // TODO: 세션 검증 로직 구현 (예: DB 또는 캐시에서 세션 ID 확인)
        return true; // 임시로 항상 유효하다고 설정
    }
}
