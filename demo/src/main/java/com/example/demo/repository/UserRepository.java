package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // 닉네임 중복 여부 확인 메서드
    boolean existsByNickname(String nickname);
}
