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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 여부 확인
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(403).body(Map.of("error", "User is not authenticated"));
        }

        String senderNickname = authentication.getName();
        String receiverNickname = request.get("receiverNickname");
        String content = request.get("content");
        String paperColor = request.get("paperColor");

        try {
            // sender 사용자 정보 확인
            User sender = userRepository.findByNickname(senderNickname)
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
            // receiver 사용자 정보 확인
            User receiver = userRepository.findByNickname(receiverNickname)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

            // 편지 생성
            Letter letter = new Letter(sender, receiver, content, paperColor);
            letterRepository.save(letter);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Long> receivedLetterIds = letterRepository.findByReceiver(user)
                .stream()
                .map(Letter::getId)
                .collect(Collectors.toList());

        return ResponseEntity.ok(receivedLetterIds);
    }

    // 특정 편지 내용 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getLetterContent(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Letter letter = letterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Letter not found"));

        if (!letter.getReceiver().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not the recipient of this letter");
        }

        return ResponseEntity.ok(Map.of(
                "content", letter.getContent(),
                "senderNickname", letter.getSender().getNickname(),
                "paperColor", letter.getPaperColor(),
                "sentAt", letter.getSentAt().toString()
        ));
    }
}

