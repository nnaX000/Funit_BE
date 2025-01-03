package com.example.demo.service;

import com.example.demo.dto.TestRequest;
import com.example.demo.entity.Test;
import com.example.demo.repository.TestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public int saveOrUpdateTest(TestRequest request) {
        Test existingTest = testRepository.findByUserId(request.getUserId());

        try {
            if (existingTest != null) {
                List<String> existingTests = existingTest.getTests();
                existingTests.addAll(request.getTests());
                existingTest.setTests(existingTests);

                List<List<String>> existingOptions = existingTest.getOptions();
                existingOptions.addAll(request.getOptions());
                existingTest.setOptions(existingOptions);

                List<String> existingAnswers = existingTest.getAnswers();
                existingAnswers.addAll(request.getAnswers());
                existingTest.setAnswers(existingAnswers);

                testRepository.save(existingTest);
                return existingTests.size();
            } else {
                Test newTest = new Test(
                        request.getUserId(),
                        request.getTests(),
                        request.getOptions(),
                        request.getAnswers()
                );
                testRepository.save(newTest);
                return request.getTests().size();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    public Map<String, List<?>> getTestsAndOptionsByUserId(Long userId) {
        Test test = testRepository.findByUserId(userId);

        if (test == null) {
            throw new RuntimeException("해당 userId에 대한 데이터가 존재하지 않습니다.");
        }

        try {
            List<String> tests = test.getTests();
            List<List<String>> options = test.getOptions();
            return Map.of("tests", tests, "options", options);
        } catch (Exception e) {
            throw new RuntimeException("데이터 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
