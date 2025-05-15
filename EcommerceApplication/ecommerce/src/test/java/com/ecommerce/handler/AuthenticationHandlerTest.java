package com.ecommerce.handler;

import com.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthenticationHandlerTest {

    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private Scanner scanner;
    
    @InjectMocks
    private AuthenticationHandler authenticationHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
@Test
public void testSignUp_Success() {
    String email = "test@example.com";
    String password = "password123";
    String name = "Test User";

    User newUser = new User(System.currentTimeMillis(), email, name, password);
    when(scanner.nextLine()).thenReturn(name, email, password);
    when(restTemplate.postForEntity(
            eq("http://localhost:8080/api/users/signup"), 
            any(User.class), 
            eq(User.class))
    ).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    authenticationHandler.signUp();

    verify(restTemplate, times(1)).postForEntity(
            eq("http://localhost:8080/api/users/signup"), 
            any(User.class), 
            eq(User.class)
    );
}

@Test
public void testSignUp_EmailAlreadyExists() {
    String email = "test@example.com";
    String password = "password123";
    String name = "Test User";

    when(scanner.nextLine()).thenReturn(name, email, password);
    when(restTemplate.postForEntity(
            eq("http://localhost:8080/api/users/signup"), 
            any(User.class), 
            eq(User.class))
    ).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT));

    authenticationHandler.signUp();

    verify(restTemplate, times(1)).postForEntity(
            eq("http://localhost:8080/api/users/signup"), 
            any(User.class), 
            eq(User.class)
    );
}

@Test
public void testLogin_Success() {
    String email = "test@example.com";
    String password = "password123";
    User user = new User(System.currentTimeMillis(), email, "Test User", password);

    when(scanner.nextLine()).thenReturn(email, password);
    when(restTemplate.postForEntity(
            eq("http://localhost:8080/api/users/login?email=" + email + "&password=" + password),
            any(),
            eq(User.class))
    ).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

    Optional<User> loggedInUser = authenticationHandler.login();

    assert(loggedInUser.isPresent());
    assertEquals("Test User", loggedInUser.get().getName());
}

@Test
public void testLogout() {
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
   
    authenticationHandler.logout();

    SecurityContext context = SecurityContextHolder.getContext();
    assertNull(context.getAuthentication(), "Authentication should be null after logout");
}

    @Test
    public void testLogin_InvalidCredentials() {
        String email = "test@example.com";
        String password = "wrongpassword";

        when(scanner.nextLine()).thenReturn(email, password);
        when(restTemplate.postForEntity(any(), any(), eq(User.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<User> loggedInUser = authenticationHandler.login();

        assert(loggedInUser.isEmpty());
    }

    @Test
    public void testIsValidEmail_Valid() {
        String validEmail = "test@example.com";
        assert(authenticationHandler.isValidEmail(validEmail));
    }

    @Test
    public void testIsValidEmail_Invalid() {
        String invalidEmail = "invalid-email";
        assert(!authenticationHandler.isValidEmail(invalidEmail));
    }

    @Test
    public void testIsValidPassword_Valid() {
        String validPassword = "password123";
        assert(authenticationHandler.isValidPassword(validPassword));
    }

    @Test
    public void testIsValidPassword_Invalid() {
        String invalidPassword = "short";
        assert(!authenticationHandler.isValidPassword(invalidPassword));
    }
}
