package com.simox.pneumia_backend.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simox.pneumia_backend.dto.RegisterRequest;
import com.simox.pneumia_backend.entity.User;
import com.simox.pneumia_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username) || userRepository.existsByEmail(req.email)) {
            throw new IllegalArgumentException("Username or email already exists");
        }
        User u = new User();
        u.setUsername(req.username);
        u.setEmail(req.email);
        u.setPasswordHash(passwordEncoder.encode(req.password));
        u.setUserType(User.UserType.valueOf(req.userType));
        u.setCreatedAt(java.time.LocalDateTime.now());
        return userRepository.save(u);
    }

    public Optional<User> findByUsernameOrEmail(String v) {
        return v.contains("@") ? userRepository.findByEmail(v) : userRepository.findByUsername(v);
    }
}
