package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomSessionFilter extends OncePerRequestFilter {

    private final UserRepository userRepository; // UserRepository 주입

    public CustomSessionFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 쿠키에서 SESSIONID 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    // 세션 유효성 검증 및 사용자 정보 로드
                    if (isValidSession(sessionId)) {
                        UserDetails userDetails = loadUserBySessionId(sessionId);
                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidSession(String sessionId) {
        // 세션 ID 검증 로직 (예: DB 또는 캐시에서 세션 확인)
        // 아래는 단순한 예제입니다. 실제로는 Redis나 DB와 연동해 확인해야 합니다.
        return userRepository.existsBySessionId(sessionId); // 세션 ID가 존재하면 true 반환
    }

    private UserDetails loadUserBySessionId(String sessionId) {
        // 세션 ID를 기반으로 사용자 로드
        return userRepository.findBySessionId(sessionId)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getNickname())
                        .password(user.getPassword()) // 실제로는 필요하지 않음
                        .authorities("ROLE_USER")
                        .build())
                .orElse(null);
    }
}


