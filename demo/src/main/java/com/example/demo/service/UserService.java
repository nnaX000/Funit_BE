package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (userRepository.existsById(user.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserByNickname(String nickname) {
        return userRepository.findById(nickname);
    }

    // 닉네임 중복 여부 확인 서비스
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}
