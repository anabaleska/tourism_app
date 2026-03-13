package com.example.tourism_app.service;

import com.example.tourism_app.dto.UserRegistrationDto;
import com.example.tourism_app.model.User;

import java.util.Optional;

public interface UserService {
    Long getCurrentUserId();
    Optional<User> getUserById(Long id);
    void registerUser(UserRegistrationDto dto);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
