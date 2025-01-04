package com.example.demo.controller;

import com.example.demo.dto.AnswerRequest;
import com.example.demo.dto.RecordScoreDTO;
import com.example.demo.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/answer")
    public ResponseEntity<String> saveAnswer(@RequestBody AnswerRequest request) {
        recordService.saveAnswer(request);
        return ResponseEntity.ok("Answer saved successfully.");
    }

    @PostMapping("/score")
    public ResponseEntity<Integer> calculateAndSaveScore(@RequestBody AnswerRequest request) {
        int score = recordService.calculateAndSaveScore(request.getTestedBy(), request.getCreatedBy());
        return ResponseEntity.ok(score);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<RecordScoreDTO>> getLeaderboard(@RequestParam Long createdBy) {
        List<RecordScoreDTO> leaderboard = recordService.getScoresByCreatedBy(createdBy);
        return ResponseEntity.ok(leaderboard);
    }

}

