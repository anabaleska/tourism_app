package com.example.tourism_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String id;


    @Column(unique = true)
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    private String password; // default changeme123

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public @NotBlank @Size(min = 6, max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 6, max = 20) String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8) String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setUserUuid(String userUuid) {
        this.id = userUuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}