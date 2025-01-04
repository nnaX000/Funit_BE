package com.example.demo.controller;

import com.example.demo.entity.Test;
import com.example.demo.repository.TestRepository;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/random_test")
public class TestController {
    private final TestRepository testRepository;

    @Autowired
    private TestService testService;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @PostMapping
    public ResponseEntity<Test> saveTest(@RequestBody Test test) {
        Test savedTest = testRepository.save(test);
        return ResponseEntity.ok(savedTest);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getTestsAndOptionsByUserId(@PathVariable Long userId) {
        // `Map<String, List<?>>` -> `Map<String, Object>`로 변환하여 반환
        Map<String, Object> response = (Map<String, Object>) (Map) testService.getTestsAndOptionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

}


