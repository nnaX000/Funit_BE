package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository; // UserRepository import 추가
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // 모든 도메인 허용
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // UserRepository를 생성자로 주입
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<User> getUser(@PathVariable String nickname) {
        return userService.getUserByNickname(nickname)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 닉네임 중복 체크 엔드포인트
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = userService.isNicknameAvailable(nickname);

        // 응답 JSON 형태 생성
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);

        return ResponseEntity.ok(response);
    }

    // 자신의 ID와 닉네임 반환
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        // 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body(Map.of("error", "User is not authenticated"));
        }

        String username = authentication.getName(); // 현재 인증된 사용자의 username(nickname)

        // User 정보 조회
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "nickname", user.getNickname()
        ));
    }


}
