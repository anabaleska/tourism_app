package com.example.tourism_app.service;

import com.example.tourism_app.dto.UserRegistrationDto;
import com.example.tourism_app.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    String getCurrentUserId();
    Optional<User> getUserById(String id);
    void registerUser(UserRegistrationDto dto);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean updatePassword(String currentUserId, String password, String cpassword);

    boolean deleteUser(String currentUserId, String password);
}
