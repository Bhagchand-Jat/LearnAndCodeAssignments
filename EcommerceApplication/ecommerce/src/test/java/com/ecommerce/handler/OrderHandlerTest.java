package com.ecommerce.handler;

import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderHandlerTest {

    private RestTemplate restTemplate;
    private Scanner scanner;
    private OrderHandler orderHandler;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        scanner = mock(Scanner.class);
        orderHandler = new OrderHandler(restTemplate, userId, scanner);
    }

    @Test
    public void testPlaceOrderSuccess() {
        when(scanner.nextLong()).thenReturn(101L);
        when(restTemplate.postForObject(anyString(), eq(null), eq(Order.class)))
            .thenReturn(new Order(BigDecimal.valueOf(40), LocalDateTime.now(), new User(userId, null, null, null), new Product(userId, null, null, null)));

        assertDoesNotThrow(() -> orderHandler.placeOrder());
    }

    @Test
    public void testPlaceOrderProductNotFound() {
        when(scanner.nextLong()).thenReturn(999L);
        when(restTemplate.postForObject(anyString(), eq(null), eq(Order.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertDoesNotThrow(() -> orderHandler.placeOrder());
    }

    @Test
    public void testPlaceOrderGenericError() {
        when(scanner.nextLong()).thenReturn(123L);
        when(restTemplate.postForObject(anyString(), eq(null), eq(Order.class)))
            .thenThrow(new RuntimeException("Server error"));

        assertDoesNotThrow(() -> orderHandler.placeOrder());
    }
   

    @Test
    public void testViewOrderHistorySuccess() {
        Order[] orders = {
          new Order(BigDecimal.valueOf(40), LocalDateTime.now(), new User(userId, null, null, null), new Product(userId, null, null, null)),
          new Order(BigDecimal.valueOf(40), LocalDateTime.now(), new User(userId, null, null, null), new Product(userId, null, null, null))
        };

        when(restTemplate.getForObject(anyString(), eq(Order[].class))).thenReturn(orders);

        assertDoesNotThrow(() -> orderHandler.viewOrderHistory());
    }

    @Test
    public void testViewOrderHistoryEmpty() {
        when(restTemplate.getForObject(anyString(), eq(Order[].class))).thenReturn(new Order[0]);

        assertDoesNotThrow(() -> orderHandler.viewOrderHistory());
    }

    @Test
    public void testViewOrderHistoryNotFound() {
        when(restTemplate.getForObject(anyString(), eq(Order[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertDoesNotThrow(() -> orderHandler.viewOrderHistory());
    }

    @Test
    public void testViewOrderHistoryGenericError() {
        when(restTemplate.getForObject(anyString(), eq(Order[].class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        assertDoesNotThrow(() -> orderHandler.viewOrderHistory());
    }
}
