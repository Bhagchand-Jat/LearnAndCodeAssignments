package com.ecommerce.handler;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartHandlerTest {

    private RestTemplate restTemplate;
    private Scanner scanner;
    private CartHandler cartHandler;
    private final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        scanner = mock(Scanner.class);
        cartHandler = new CartHandler(restTemplate, userId, scanner);
    }

    @Test
    public void testFetchProductReturnsProduct() {
        Product product = new Product(1L, "Product A", new BigDecimal("99.99"),new Category(1L, "Category"));
        when(restTemplate.getForObject(anyString(), eq(Product.class))).thenReturn(product);

        Optional<Product> result = cartHandler.fetchProduct(product.getId());

        assertTrue(result.isPresent());
        assertEquals("Product A", result.get().getName());
    }

    @Test
    public void testFetchProductReturnsEmptyWhenException() {
        when(restTemplate.getForObject(anyString(), eq(Product.class))).thenThrow(new RuntimeException("Error"));

        Optional<Product> result = cartHandler.fetchProduct(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFetchCartReturnsCart() {
        CartItem item = new CartItem(1L, 2, new BigDecimal("50.00"));
        Cart cart = new Cart(1L);
        cart.setItems(List.of(item));

        when(restTemplate.getForObject(anyString(), eq(Cart.class))).thenReturn(cart);

        Optional<Cart> result = cartHandler.fetchCart();

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getItems().size());
    }

    @Test
    public void testFetchCartReturnsEmptyWhenException() {
        when(restTemplate.getForObject(anyString(), eq(Cart.class))).thenThrow(new RuntimeException("Error"));

        Optional<Cart> result = cartHandler.fetchCart();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testClearCartSuccess() {
        doNothing().when(restTemplate).delete(anyString());

        assertDoesNotThrow(() -> cartHandler.clearCart());
    }

    @Test
    public void testClearCartHandlesExceptionGracefully() {
        doThrow(new RuntimeException("Failed")).when(restTemplate).delete(anyString());

        assertDoesNotThrow(() -> cartHandler.clearCart());
    }
}
