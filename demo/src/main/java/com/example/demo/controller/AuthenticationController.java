package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String nickname = request.get("nickname");
        String password = request.get("password");

        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(nickname, password)
        );

        // SecurityContextHolder에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpRequest.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // 사용자 정보 조회
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 세션 ID 가져오기
        String sessionId = httpRequest.getSession().getId();

        // JSON 응답에 세션 ID 포함
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "id", String.valueOf(user.getId()),
                "nickname", user.getNickname(),
                "sessionId", sessionId
        ));
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
