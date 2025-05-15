package com.ecommerce.handler;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

class CartHandlerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private CartHandler cartHandler;

    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartHandler = new CartHandler(restTemplate, userId, scanner);
    }

    @Test
    void testAddProductToCart_ProductExists() {
        when(scanner.nextLong()).thenReturn(100L);
        when(scanner.nextInt()).thenReturn(2);
        when(scanner.nextLine()).thenReturn("");

        Product product = new Product();
        product.setId(100L);
        product.setPrice(BigDecimal.valueOf(50));

        when(restTemplate.getForObject(anyString(), eq(Product.class))).thenReturn(product);
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn("Added to cart");

        cartHandler.addProductToCart();

        verify(restTemplate).postForObject(anyString(), any(), eq(String.class));
    }

    @Test
    void testAddProductToCart_ProductNotFound() {
        when(scanner.nextLong()).thenReturn(999L);
        when(scanner.nextInt()).thenReturn(1);
        when(scanner.nextLine()).thenReturn("");

        when(restTemplate.getForObject(anyString(), eq(Product.class))).thenReturn(null);

        cartHandler.addProductToCart();

        verify(restTemplate, never()).postForObject(anyString(), any(), eq(String.class));
    }


    @Test
    void testViewCart_CartHasItems() {
        CartItem item = new CartItem(100L, 2, BigDecimal.valueOf(50));
        Cart cart = new Cart();
        cart.setItems(List.of(item));
        cart.setTotalPrice(BigDecimal.valueOf(100));

        when(restTemplate.getForObject(anyString(), eq(Cart.class))).thenReturn(cart);

        cartHandler.viewCart();

        verify(restTemplate).getForObject(anyString(), eq(Cart.class));
    }

    @Test
    void testViewCart_CartIsEmpty() {
        Cart cart = new Cart();
        cart.setItems(Collections.emptyList());

        when(restTemplate.getForObject(anyString(), eq(Cart.class))).thenReturn(cart);

        cartHandler.viewCart();

        verify(restTemplate).getForObject(anyString(), eq(Cart.class));
    }

    @Test
    void testRemoveProductFromCart() {
        when(scanner.nextLong()).thenReturn(100L);

        doNothing().when(restTemplate).delete(anyString());

        cartHandler.removeProductFromCart();

        verify(restTemplate).delete(anyString());
    }

    @Test
    void testPlaceOrderFromCart_CartHasItems_Confirmed() {
        CartItem item = new CartItem(100L, 1, BigDecimal.valueOf(50));
        Cart cart = new Cart();
        cart.setItems(List.of(item));
        cart.setTotalPrice(BigDecimal.valueOf(50));

        when(restTemplate.getForObject(contains("/cart/view/"), eq(Cart.class))).thenReturn(cart);
        when(scanner.nextLine()).thenReturn("y");

        Order order = new Order();
        when(restTemplate.postForObject(contains("/orders"), isNull(), eq(Order.class))).thenReturn(order);
        doNothing().when(restTemplate).delete(contains("/cart/clear/"));

        cartHandler.placeOrderFromCart();

        verify(restTemplate).postForObject(contains("/orders"), isNull(), eq(Order.class));
        verify(restTemplate).delete(contains("/cart/clear/"));
    }

    @Test
    void testPlaceOrderFromCart_CartIsEmpty() {
        Cart cart = new Cart();
        cart.setItems(Collections.emptyList());

        when(restTemplate.getForObject(contains("/cart/view/"), eq(Cart.class))).thenReturn(cart);

        cartHandler.placeOrderFromCart();

        verify(restTemplate, never()).postForObject(anyString(), any(), eq(Order.class));
    }

    @Test
    void testClearCart() {
        doNothing().when(restTemplate).delete(anyString());

        cartHandler.clearCart();

        verify(restTemplate).delete(anyString());
    }
}
