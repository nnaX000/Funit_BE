package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String nickname = request.get("nickname");
        String password = request.get("password");

        try {
            // 사용자 인증 처리
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(nickname, password)
            );

            // 사용자 정보 조회
            User user = userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // 세션 ID 생성 (커스텀 로직 또는 기본 HttpSession 사용 가능)
            String sessionId = UUID.randomUUID().toString(); // 또는 HttpSession 기반으로 생성 가능
            // 세션 ID를 쿠키에 저장
            Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효
            response.addCookie(sessionCookie);

            // JSON 응답 생성
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("id", user.getId());
            responseBody.put("nickname", user.getNickname());

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            // 예외 처리 및 에러 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Login failed",
                    "error", e.getMessage()
            ));
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(403).body(Map.of("error", "User is not authenticated"));
        }

        String username = authentication.getName();

        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(Map.of("id", user.getId(), "nickname", user.getNickname()));
    }
}
