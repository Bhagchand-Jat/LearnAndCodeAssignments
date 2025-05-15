package com.ecommerce.controllers;

import com.ecommerce.controller.OrderController;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_SuccessStatus() {
        Long userId = 1L;
        Long productId = 1L;

        User user = new User();
        Product product = new Product();
        product.setPrice(new BigDecimal("50.00"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Order> response = orderController.placeOrder(userId, productId);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testPlaceOrder_SuccessOrderSaved() {
        Long userId = 2L;
        Long productId = 2L;

        User user = new User();
        Product product = new Product();
        product.setPrice(new BigDecimal("99.99"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Order savedOrder = new Order(product.getPrice(), LocalDateTime.now().plusDays(5), user, product);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        ResponseEntity<Order> response = orderController.placeOrder(userId, productId);

        assertEquals(savedOrder, response.getBody());
    }

    @Test
    void testPlaceOrder_UserNotFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseEntity<Order> response = orderController.placeOrder(100L, 200L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testPlaceOrder_ProductNotFound() {
        Long userId = 1L;
        Long productId = 200L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ResponseEntity<Order> response = orderController.placeOrder(userId, productId);

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void testGetOrderHistory_ReturnsOkStatus() {
        Long userId = 10L;
        when(orderRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Order>> response = orderController.getOrderHistory(userId);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetOrderHistory_ReturnsOrderList() {
        Long userId = 11L;
        List<Order> mockOrders = List.of(new Order(), new Order());

        when(orderRepository.findByUserId(userId)).thenReturn(mockOrders);

        ResponseEntity<List<Order>> response = orderController.getOrderHistory(userId);

        assertEquals(mockOrders, response.getBody());
    }
}
