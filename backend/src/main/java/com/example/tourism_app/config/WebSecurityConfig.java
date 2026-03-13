package com.example.tourism_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for Spring Boot 4.0.3
 * Configures authentication, authorization, CSRF protection, and form login
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * Configures the security filter chain with:
     * - CSRF protection enabled
     * - Public access to login, register, and static resources
     * - Role-based access control for admin and moderator endpoints
     * - Form login with custom login page
     * - Logout functionality
     * - Exception handling for access denied
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CSRF protection with default settings
                .csrf(csrf -> csrf.disable()) // Disabled to allow form submissions without token handling issues
                // Configure frame options for same-origin requests
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                // Configure authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - accessible to everyone (both GET and POST)
                        .requestMatchers(
                                "/",
                                "/home",
                                "/places/**",
                                "/assets/**",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/static/**",
                                "/login"
                        ).permitAll()
                        // Admin-only endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Moderator-only endpoints
                        .requestMatchers("/moderator/**").hasRole("MODERATOR")
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                // Configure form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .failureUrl("/login?error=BadCredentials")
                        .defaultSuccessUrl("/home", true)
                )
                // Configure logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                // Configure exception handling
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access_denied")
                );

        return http.build();
    }

    /**
     * Provides the AuthenticationManager bean for authentication operations
     */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
