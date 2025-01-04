package com.example.demo.service;

import com.example.demo.dto.AnswerRequest;
import com.example.demo.dto.RecordScoreDTO;
import com.example.demo.entity.Test;
import com.example.demo.entity.UserRecord;
import com.example.demo.repository.RecordRepository;
import com.example.demo.repository.TestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private TestRepository testRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void saveAnswer(Long testedBy, Long createdBy, String answer) {
        UserRecord record = recordRepository.findByTestedByAndCreatedBy(testedBy, createdBy);
        try {
            if (record != null) {
                List<String> currentAnswers = objectMapper.readValue(record.getAnswer(), List.class);
                currentAnswers.add(answer);
                record.setAnswer(objectMapper.writeValueAsString(currentAnswers));
            } else {
                record = new UserRecord();
                record.setTestedBy(testedBy);
                record.setCreatedBy(createdBy);

                List<String> answers = new ArrayList<>();
                answers.add(answer);
                record.setAnswer(objectMapper.writeValueAsString(answers));
            }
            recordRepository.save(record);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    public void saveAnswer(AnswerRequest request) {
        saveAnswer(request.getTestedBy(), request.getCreatedBy(), request.getAnswer());
    }

    public int calculateAndSaveScore(Long testedBy, Long createdBy) {
        UserRecord record = recordRepository.findByTestedByAndCreatedBy(testedBy, createdBy);
        Test test = testRepository.findByUserId(createdBy);

        if (record == null || test == null) {
            throw new RuntimeException("Record 또는 Test 데이터가 없습니다.");
        }

        try {
            List<String> correctAnswers = objectMapper.readValue(test.getCorrectAnswer(), List.class);
            List<String> userAnswers = objectMapper.readValue(record.getAnswer(), List.class);

            int score = 0;
            for (int i = 0; i < Math.min(correctAnswers.size(), userAnswers.size()); i++) {
                if (correctAnswers.get(i).equals(userAnswers.get(i))) {
                    score++;
                }
            }

            // 점수 저장
            record.setScore(score);
            recordRepository.save(record);

            return score;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    public List<RecordScoreDTO> getScoresByCreatedBy(Long createdBy) {
        // RecordRepository의 findScoresByCreatedBy 메서드 호출
        return recordRepository.findScoresByCreatedBy(createdBy);
    }
}
