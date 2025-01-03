package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @JsonIgnore // 이 필드를 응답에서 제외
    @Column(nullable = false, columnDefinition = "TEXT")
    private String options; // JSON 문자열로 저장됨

    @Transient
    private List<String> optionList; // JSON을 변환한 결과를 저장

    public void loadOptions() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.optionList = mapper.readValue(this.options, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }
}
