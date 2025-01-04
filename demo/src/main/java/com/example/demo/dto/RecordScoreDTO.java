package com.example.demo.dto;

public class RecordScoreDTO {
    private String testedByNickname;
    private int score;

    public RecordScoreDTO(String testedByNickname, int score) {
        this.testedByNickname = testedByNickname;
        this.score = score;
    }

    public String getTestedByNickname() {
        return testedByNickname;
    }

    public void setTestedByNickname(String testedByNickname) {
        this.testedByNickname = testedByNickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
