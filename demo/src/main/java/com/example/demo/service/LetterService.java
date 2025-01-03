package com.example.demo.service;

import com.example.demo.entity.Letter;
import com.example.demo.repository.LetterRepository;
import org.springframework.stereotype.Service;

@Service
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterService(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    public Letter sendLetter(String senderNickname, String receiverNickname, String content, String paperColor) {
        // 편지지 색상 검증
        if (!isValidPaperColor(paperColor)) {
            throw new IllegalArgumentException("Invalid paper color. Choose from 'red', 'blue', or 'yellow'.");
        }

        Letter letter = new Letter(senderNickname, receiverNickname, content, paperColor);
        return letterRepository.save(letter);
    }

    private boolean isValidPaperColor(String color) {
        return color.equals("red") || color.equals("blue") || color.equals("yellow");
    }
}
