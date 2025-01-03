package com.example.demo.controller;

import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/api/questions")
    public List<Question> getQuestions() {
        return questionRepository.findAll().stream().map(question -> {
            // JSON 문자열을 Java 객체로 변환된 optionList를 설정
            question.loadOptions();
            return question;
        }).collect(Collectors.toList());
    }
}
