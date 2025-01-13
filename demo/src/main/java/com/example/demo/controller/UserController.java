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

    // ID로 사용자 조회
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
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

}
