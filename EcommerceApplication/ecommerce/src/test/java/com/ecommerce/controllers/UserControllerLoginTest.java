package com.ecommerce.controllers;

import com.ecommerce.controller.UserController;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerLoginTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testLogin_shouldReturn200OnValidCredentials() {
        String email = "test@example.com";
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.login(email, rawPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogin_shouldReturnUserObjectWhenSuccess() {
        String email = "test@example.com";
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.login(email, rawPassword);

        assertEquals(user, response.getBody());
    }

    @Test
    void testLogin_shouldReturn404WhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.login("test@example.com", "password");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testLogin_shouldReturn404WhenPasswordMismatch() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("correctPassword"));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.login(email, "wrongPassword");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
