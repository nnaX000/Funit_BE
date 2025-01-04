package com.example.demo.dto;

public class AnswerRequest {
    private Long testedBy;  // 문제를 푸는 사람 ID
    private Long createdBy; // 테스트를 만든 사람 ID
    private String answer;  // 선택한 답안

    // Getters and Setters
    public Long getTestedBy() {
        return testedBy;
    }

    public void setTestedBy(Long testedBy) {
        this.testedBy = testedBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

