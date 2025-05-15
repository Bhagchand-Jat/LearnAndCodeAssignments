package com.ecommerce.controllers;

import com.ecommerce.controller.UserController;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerGetUserTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByEmail_shouldReturn200WhenFound() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetUserByEmail_shouldReturnUserWhenFound() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail(email);

        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserByEmail_shouldReturn404WhenNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByEmail("notfound@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
