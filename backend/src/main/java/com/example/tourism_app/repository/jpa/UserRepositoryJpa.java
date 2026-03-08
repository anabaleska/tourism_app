package com.example.tourism_app.repository.jpa;

import com.example.tourism_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepositoryJpa extends JpaRepository<User, Long> {
    User getUserById(Long id);
//    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User getUserByUserUuid(UUID userUuid);
}
