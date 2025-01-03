package com.example.demo.service;

import com.example.demo.dto.TestRequest;
import com.example.demo.entity.Test;
import com.example.demo.repository.TestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public int saveOrUpdateTest(TestRequest request) {
        // DB에서 user_id로 Test 데이터 찾기
        Test existingTest = testRepository.findByUserId(request.getUserId());

        try {
            if (existingTest != null) {
                // 기존 데이터가 있다면 JSON 데이터를 리스트로 변환 후 병합
                List<String> existingTests = existingTest.getTests();
                existingTests.addAll(request.getTests());
                existingTest.setTests(existingTests);

                List<List<String>> existingOptions = existingTest.getOptions();
                existingOptions.addAll(request.getOptions());
                existingTest.setOptions(existingOptions);

                List<String> existingAnswers = existingTest.getAnswers();
                existingAnswers.addAll(request.getAnswers());
                existingTest.setAnswers(existingAnswers);

                // 병합된 데이터 저장
                testRepository.save(existingTest);

                // 업데이트 후 `tests` 컬럼의 길이 반환
                return existingTests.size();
            } else {
                // 기존 데이터가 없다면 새로운 데이터 추가
                Test newTest = new Test(
                        request.getUserId(),
                        request.getTests(),
                        request.getOptions(),
                        request.getAnswers()
                );
                testRepository.save(newTest);

                // 새로 추가된 데이터의 `tests` 개수 반환
                return request.getTests().size();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }
}
