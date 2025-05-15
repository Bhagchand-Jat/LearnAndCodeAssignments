package com.ecommerce.controllers;

import com.ecommerce.controller.CartController;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartControllerTest {

    private CartController cartController;

    @BeforeEach
    void setUp() {
        cartController = new CartController();
    }

    @Test
    void testAddToCart_ReturnsCreatedStatus() {
        CartItem item = new CartItem(1L, 2, new BigDecimal("10.00"));
        ResponseEntity<String> response = cartController.addToCart(1L, item);
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testAddToCart_ReturnsSuccessMessage() {
        CartItem item = new CartItem(2L, 1, new BigDecimal("5.00"));
        ResponseEntity<String> response = cartController.addToCart(2L, item);
        assertEquals("Product added to cart successfully!", response.getBody());
    }

    @Test
    void testViewCart_ReturnsOkForExistingCart() {
        Long userId = 3L;
        cartController.addToCart(userId, new CartItem(1L, 1, new BigDecimal("15.00")));
        ResponseEntity<Cart> response = cartController.viewCart(userId);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testViewCart_ReturnsNotFoundForMissingCart() {
        ResponseEntity<Cart> response = cartController.viewCart(99L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testClearCart_ReturnsSuccessMessage() {
        Long userId = 4L;
        cartController.addToCart(userId, new CartItem(2L, 2, new BigDecimal("8.00")));
        ResponseEntity<String> response = cartController.clearCart(userId);
        assertEquals("Items removed from cart successfully!", response.getBody());
    }

    @Test
    void testClearCart_ReturnsNotFoundMessage() {
        ResponseEntity<String> response = cartController.clearCart(404L);
        assertEquals("Cart not found!", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_ReturnsSuccessMessage() {
        Long userId = 5L;
        Long productId = 3L;
        cartController.addToCart(userId, new CartItem(productId, 1, new BigDecimal("30.00")));
        ResponseEntity<String> response = cartController.deleteProductFromCart(userId, productId);
        assertEquals("Item removed from cart successfully!", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_ReturnsCartNotFound() {
        ResponseEntity<String> response = cartController.deleteProductFromCart(123L, 456L);
        assertEquals("Cart not found!", response.getBody());
    }

    @Test
    void testDeleteProductFromCart_ReturnsItemNotFound() {
        Long userId = 6L;
        cartController.addToCart(userId, new CartItem(7L, 1, new BigDecimal("12.00")));
        ResponseEntity<String> response = cartController.deleteProductFromCart(userId, 99L);
        assertEquals("No item found in cart with this product ID.", response.getBody());
    }
}
