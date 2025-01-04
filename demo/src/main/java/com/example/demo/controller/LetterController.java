package com.example.demo.controller;

import com.example.demo.entity.Letter;
import com.example.demo.service.LetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    @PostMapping
    public ResponseEntity<?> sendLetter(@RequestBody Map<String, String> request) {
        String senderNickname = request.get("senderNickname");
        String receiverNickname = request.get("receiverNickname");
        String content = request.get("content");
        String paperColor = request.get("paperColor");

        try {
            Letter letter = letterService.sendLetter(senderNickname, receiverNickname, content, paperColor);
            return ResponseEntity.ok(Map.of("message", "Letter sent successfully!", "letter", letter));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
