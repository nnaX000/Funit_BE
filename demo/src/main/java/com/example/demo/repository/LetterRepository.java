package com.example.demo.repository;

import com.example.demo.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    // 기본적인 CRUD 기능 제공
}
