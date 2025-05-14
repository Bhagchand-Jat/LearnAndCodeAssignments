package com.ecommerce.controllers;

import com.ecommerce.controller.UserController;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerSignUpTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock behavior for "email exists"
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Mock behavior for save
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void testSignUp_shouldReturn201WhenSuccess() {
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("password");

        // Override the default email check for this test
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);

        ResponseEntity<User> response = userController.signUp(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testSignUp_shouldReturnHashedPassword() {
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);

        ResponseEntity<User> response = userController.signUp(user);

        assertNotEquals("password", response.getBody().getPassword());
    }

    @Test
    void testSignUp_shouldReturn409WhenEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        ResponseEntity<User> response = userController.signUp(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
