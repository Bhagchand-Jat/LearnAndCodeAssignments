package com.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setTotalPrice(new BigDecimal("1000.00"));

        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(101L, 2, new BigDecimal("500.00")));
        cart.setItems(items);

        assertThat(cart.getUserId()).isEqualTo(1L);
        assertThat(cart.getTotalPrice()).isEqualByComparingTo("1000.00");
        assertThat(cart.getItems()).hasSize(1);
    }

    @Test
    void testParameterizedConstructor() {
        Cart cart = new Cart(2L);
        assertThat(cart.getUserId()).isEqualTo(2L);
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotalPrice()).isEqualByComparingTo("0.00");
    }

    @Test
    void testAddItemsAndCalculateTotal() {
        Cart cart = new Cart(3L);
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(201L, 1, new BigDecimal("300.00")));
        items.add(new CartItem(202L, 2, new BigDecimal("150.00")));
        cart.setItems(items);

        BigDecimal total = items.stream()
                .map(cartItem->cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);

        assertThat(cart.getItems()).hasSize(2);
        assertThat(cart.getTotalPrice()).isEqualByComparingTo("600.00");
    }

    @Test
    void testToString() {
        Cart cart = new Cart(4L);
        cart.setTotalPrice(new BigDecimal("200.00"));

        String result = cart.toString();
        assertThat(result).contains("userId=4");
        assertThat(result).contains("totalPrice=200.00");
    }
}
