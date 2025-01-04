package com.example.demo.service;

import com.example.demo.entity.Letter;
import com.example.demo.entity.User;
import com.example.demo.repository.LetterRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    public LetterService(LetterRepository letterRepository, UserRepository userRepository) {
        this.letterRepository = letterRepository;
        this.userRepository = userRepository;
    }

    public Letter sendLetter(String senderNickname, String receiverNickname, String content, String paperColor) {
        // 발신자와 수신자 찾기
        User sender = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findByNickname(receiverNickname)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // 편지지 색상 검증
        if (!isValidPaperColor(paperColor)) {
            throw new IllegalArgumentException("Invalid paper color. Choose from 'red', 'blue', or 'yellow'.");
        }

        Letter letter = new Letter(sender, receiver, content, paperColor);
        return letterRepository.save(letter);
    }

    private boolean isValidPaperColor(String color) {
        return color.equals("red") || color.equals("blue") || color.equals("yellow");
    }
}
