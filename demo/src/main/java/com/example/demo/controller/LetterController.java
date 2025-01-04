package com.example.demo.controller;

import com.example.demo.entity.Letter;
import com.example.demo.service.LetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> sendLetter(@RequestBody Map<String, String> request) {
        String senderNickname = request.get("senderNickname");
        String receiverNickname = request.get("receiverNickname");
        String content = request.get("content");
        String paperColor = request.get("paperColor");

        Letter letter = letterService.sendLetter(senderNickname, receiverNickname, content, paperColor);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Letter sent successfully!");
        response.put("letter", letter);

        return ResponseEntity.ok(response);
    }
}
