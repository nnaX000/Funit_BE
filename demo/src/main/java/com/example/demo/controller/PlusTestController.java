package com.example.demo.controller;

import com.example.demo.dto.TestRequest;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plus_test")
public class PlusTestController {

    @Autowired
    private TestService testService;

    @PostMapping
    public ResponseEntity<Integer> saveOrUpdateTest(@RequestBody TestRequest request) {
        int updatedTestCount = testService.saveOrUpdateTest(request);
        return ResponseEntity.ok(updatedTestCount); // `tests` 리스트 길이 반환
    }
}

