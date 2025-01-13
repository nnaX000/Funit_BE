package com.example.demo.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "record")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tested_by", nullable = false)
    private Long testedBy;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "score", nullable = false)
    private int score;

    @Lob
    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;


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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
