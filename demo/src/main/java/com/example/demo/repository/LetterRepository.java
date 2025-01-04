package com.example.demo.repository;

import com.example.demo.entity.Letter;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    List<Letter> findByReceiver(User receiver);
}
