package com.example.demo.controller;

import com.example.demo.entity.Letter;
import com.example.demo.entity.User;
import com.example.demo.repository.LetterRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;
    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    public LetterController(LetterService letterService, LetterRepository letterRepository, UserRepository userRepository) {
        this.letterService = letterService;
        this.letterRepository = letterRepository;
        this.userRepository = userRepository;
    }

    // 편지 보내기
    @PostMapping
    public ResponseEntity<?> sendLetter(@RequestBody Map<String, String> request) {
        String senderNickname = request.get("senderNickname");
        String receiverNickname = request.get("receiverNickname");
        String content = request.get("content");
        String paperColor = request.get("paperColor");

        try {
            Letter letter = letterService.sendLetter(senderNickname, receiverNickname, content, paperColor);
            return ResponseEntity.ok(Map.of(
                    "message", "Letter sent successfully!",
                    "letterId", letter.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 받은 편지 ID 목록 조회
    @GetMapping("/received")
    public ResponseEntity<List<Long>> getReceivedLetters() {
        // 현재 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 사용자 조회
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 받은 편지 목록 가져오기
        List<Long> receivedLetterIds = letterRepository.findByReceiver(user)
                .stream()
                .map(Letter::getId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(receivedLetterIds);
    }

    // 특정 편지 내용 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getLetterContent(@PathVariable Long id) {
        // 현재 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 사용자 조회
        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 편지 조회
        Letter letter = letterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Letter not found"));

        // 수신자가 현재 사용자인지 확인
        if (!letter.getReceiver().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not the recipient of this letter");
        }

        // 편지 내용 반환
        return ResponseEntity.ok(Map.of(
                "content", letter.getContent(),
                "senderNickname", letter.getSender().getNickname(),
                "paperColor", letter.getPaperColor(),
                "sentAt", letter.getSentAt().toString()
        ));
    }
}
