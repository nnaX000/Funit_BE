package com.example.demo.service;

import com.example.demo.entity.Letter;
import com.example.demo.entity.User;
import com.example.demo.repository.LetterRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public LetterService(LetterRepository letterRepository, UserRepository userRepository) {
        this.letterRepository = letterRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    public Letter sendLetter(String senderNickname, String receiverNickname, String content, String paperColor) {
        // 발신자 확인
        User sender = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        // 수신자 확인
        User receiver = userRepository.findByNickname(receiverNickname)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // 점수 검증
        if (!isScoreSufficient(sender.getId(), receiver.getId())) {
            throw new IllegalArgumentException("Score is too low to send a letter. Minimum score: 7");
        }

        // 편지지 색상 검증
        if (!isValidPaperColor(paperColor)) {
            throw new IllegalArgumentException("Invalid paper color. Choose from 'red', 'blue', or 'yellow'.");
        }

        // Letter 객체 생성 및 저장
        Letter letter = new Letter(sender, receiver, content, paperColor);
        return letterRepository.save(letter);
    }

    private boolean isValidPaperColor(String color) {
        return color.equalsIgnoreCase("red") || color.equalsIgnoreCase("blue") || color.equalsIgnoreCase("yellow");
    }

    private boolean isScoreSufficient(Long senderId, Long receiverId) {
        String apiUrl = "http://localhost:8080/api/record/score";
        Map<String, Object> request = new HashMap<>();
        request.put("testedBy", senderId);
        request.put("createdBy", receiverId);

        try {
            Integer score = restTemplate.postForObject(apiUrl, request, Integer.class);
            return score != null && score >= 7;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to verify score: " + e.getMessage());
        }
    }
}
