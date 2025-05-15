package com.ecommerce;

import com.ecommerce.handler.AuthenticationHandler;
import com.ecommerce.handler.CartHandler;
import com.ecommerce.handler.OrderHandler;
import com.ecommerce.handler.ProductHandler;
import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EcommerceApplicationTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Scanner scanner;

    @Mock
    private AuthenticationHandler authenticationHandler;

    @Mock
    private CartHandler cartHandler;

    @Mock
    private OrderHandler orderHandler;

    @Mock
    private ProductHandler productHandler;

    @Mock
    private Optional<User> user;

    @InjectMocks
    private EcommerceApplication ecommerceApplication;

    @Test
    void testRunSignUp() {
        when(scanner.nextInt()).thenReturn(1);
        when(scanner.nextLine()).thenReturn("test@example.com", "password123", "Test User");
        // when(authenticationHandler.signUp()).thenReturn(null);
        when(authenticationHandler.login()).thenReturn(Optional.of(new User(1L, "test@example.com", "Test User", "password123")));
        ecommerceApplication.run();
        verify(authenticationHandler).signUp();
    }

    @Test
    void testRunLogin() {
        when(scanner.nextInt()).thenReturn(2);
        when(scanner.nextLine()).thenReturn("test@example.com", "password123");
        when(authenticationHandler.login()).thenReturn(Optional.of(new User(1L, "test@example.com", "Test User", "password123")));
        when(scanner.nextInt()).thenReturn(3);
        // when(orderHandler.placeOrder()).thenReturn(null);
        ecommerceApplication.run();
        verify(authenticationHandler).login();
        verify(orderHandler).placeOrder();
    }

    @Test
    void testRunExit() {
        when(scanner.nextInt()).thenReturn(3);
        doNothing().when(ecommerceApplication).exitApplication();
        ecommerceApplication.run();
        verify(ecommerceApplication).exitApplication();
    }

    @Test
    void testHandleLoggedInUser() {
        when(user.isPresent()).thenReturn(true);
        when(user.get().getId()).thenReturn(1L);
        when(scanner.nextInt()).thenReturn(5);
        // when(cartHandler.manageCart()).thenReturn(null);
        ecommerceApplication.handleLoggedInUser(authenticationHandler);
        verify(cartHandler).manageCart();
    }
}
