package com.example.demo.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tested_by", nullable = false)
    private Long testedBy; // 테스트를 진행한 사용자 ID

    @Column(name = "created_by", nullable = false)
    private Long createdBy; // 테스트를 생성한 사용자 ID

    @Column(name = "score")
    private Integer score; // 최종 점수

    @Lob
    @Column(name = "answer", nullable = false)
    private String answer; // 사용자가 제출한 답변(JSON 형태)

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getAnswerAsList() throws JsonProcessingException {
        return objectMapper.readValue(this.answer, List.class);
    }

    public void setAnswerFromList(List<String> answers) throws JsonProcessingException {
        this.answer = objectMapper.writeValueAsString(answers);
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
