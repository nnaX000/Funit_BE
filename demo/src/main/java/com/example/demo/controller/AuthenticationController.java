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
import org.springframework.security.core.userdetails.UserDetails;
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

            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Principal 값 확인
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 사용자 정보 조회
            User user = userRepository.findByNickname(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // 세션 ID 생성 및 설정
            String sessionId = UUID.randomUUID().toString();
            user.setSessionId(sessionId); // 세션 ID를 사용자 엔티티에 저장
            userRepository.save(user); // 데이터베이스에 저장

            // 쿠키에 세션 ID 설정
            Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(true); // HTTPS에서만 전송
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
    public ResponseEntity<?> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 여부 확인
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(403).body(Map.of("error", "User is not authenticated"));
        }

        // Principal 값 확인 및 처리
        Object principal = authentication.getPrincipal();
        System.out.println("Principal: " + principal);

        String nickname;

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            nickname = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof String) {
            nickname = (String) principal;
        } else {
            return ResponseEntity.status(500).body(Map.of("error", "Invalid Principal type"));
        }

        return fetchUserByNickname(nickname);
    }


    private ResponseEntity<?> fetchUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(user -> ResponseEntity.ok(Map.of("id", user.getId(), "nickname", user.getNickname())))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

}
