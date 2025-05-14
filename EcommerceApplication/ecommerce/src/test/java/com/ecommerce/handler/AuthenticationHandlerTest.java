package com.ecommerce.handler;

import com.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationHandlerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private AuthenticationHandler authenticationHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Successful() {
        String email = "test@example.com";
        String password = "password";
        User mockUser = new User(1L, email, "Test User", password);

        when(scanner.nextLine()).thenReturn(email).thenReturn(password);
        when(restTemplate.postForEntity(
                contains("/users/login"),
                isNull(),
                eq(User.class)))
                .thenReturn(new ResponseEntity<>(mockUser, HttpStatus.OK));

        Optional<User> result = authenticationHandler.login();

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testLogin_InvalidCredentials() {
        String email = "test@example.com";
        String password = "wrongpassword";

        when(scanner.nextLine()).thenReturn(email).thenReturn(password);
        when(restTemplate.postForEntity(
                contains("/users/login"),
                isNull(),
                eq(User.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<User> result = authenticationHandler.login();

        assertTrue(result.isEmpty());
    }

    @Test
    void testLogout_ShouldClearSecurityContext() {
        Optional<User> result = authenticationHandler.logout();
        assertTrue(result.isEmpty());
    }
}
