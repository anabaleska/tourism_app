package com.example.tourism_app.service.impl;

import com.example.tourism_app.dto.UserRegistrationDto;
import com.example.tourism_app.model.Role;
import com.example.tourism_app.model.User;
import com.example.tourism_app.repository.jpa.UserRepositoryJpa;
import com.example.tourism_app.security.CustomUserDetails;
import com.example.tourism_app.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryJpa userRepositoryJpa;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepositoryJpa userRepositoryJpa, PasswordEncoder passwordEncoder) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.passwordEncoder = passwordEncoder;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepositoryJpa.findById(getCurrentUserId());
    }

    @Override
    public void registerUser(UserRegistrationDto dto) {
        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRole(Role.USER);
        user.setUserUuid(java.util.UUID.randomUUID());

//        if (dto.getSelectedRole().equals(Role.PROFESSIONAL)) {
//            user.setRole(Role.PENDING_PROFESSIONAL);
//            user.setProfessional(true);
//        } else {
//            user.setRole(Role.PARENT);
//            user.setProfessional(false);
//        }

        userRepositoryJpa.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepositoryJpa.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryJpa.findByEmail(email).isPresent();
    }
}
