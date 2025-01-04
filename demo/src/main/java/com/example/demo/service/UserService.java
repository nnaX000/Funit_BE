package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 생성 서비스 (비밀번호 암호화 추가)
    public User createUser(User user) {
        if (userRepository.existsByNickname(user.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    // 닉네임으로 사용자 조회 서비스
    public Optional<User> getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    // 닉네임 중복 여부 확인 서비스
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}
