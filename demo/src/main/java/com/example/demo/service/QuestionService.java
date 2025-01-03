package com.example.demo.service;

import com.example.demo.dto.TestRequest;
import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.TestRequest;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    private TestRequest testRequest;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    }

