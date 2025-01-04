package com.example.demo.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Lob
    @Column(name = "tests", nullable = false)
    private String tests; // JSON 형태의 문자열로 저장

    @Lob
    @Column(name = "options", nullable = false)
    private String options; // JSON 형태의 문자열로 저장

    @Lob
    @Column(name = "correct_answer", nullable = false)
    private String correct_answer; // JSON 형태의 문자열로 저장

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 기본 생성자
    public Test() {}

    // 모든 필드를 포함한 생성자
    public Test(Long userId, List<String> tests, List<List<String>> options, List<String> correct_answer) throws JsonProcessingException {
        this.userId = userId;
        this.tests = objectMapper.writeValueAsString(tests);
        this.options = objectMapper.writeValueAsString(options);
        this.correct_answer = objectMapper.writeValueAsString(correct_answer);
    }

    // JSON 데이터 Getter: JSON 문자열 → List
    public List<String> getTests() throws JsonProcessingException {
        return objectMapper.readValue(this.tests, List.class);
    }

    public List<List<String>> getOptions() throws JsonProcessingException {
        return objectMapper.readValue(this.options, List.class);
    }

    public List<String> getAnswers() throws JsonProcessingException {
        return objectMapper.readValue(this.correct_answer, List.class);
    }

    // JSON 데이터 Setter: List → JSON 문자열
    public void setTests(List<String> tests) throws JsonProcessingException {
        this.tests = objectMapper.writeValueAsString(tests);
    }

    public void setOptions(List<List<String>> options) throws JsonProcessingException {
        this.options = objectMapper.writeValueAsString(options);
    }

    public void setAnswers(List<String> answer) throws JsonProcessingException {
        this.correct_answer = objectMapper.writeValueAsString(answer);
    }

    // 질문 개수를 반환하는 메서드
    public int getTestCount() throws JsonProcessingException {
        List<String> testsList = objectMapper.readValue(this.tests, List.class);
        return testsList.size();
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTestsAsJson() {
        return tests;
    }

    public String getOptionsAsJson() {
        return options;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }
}
