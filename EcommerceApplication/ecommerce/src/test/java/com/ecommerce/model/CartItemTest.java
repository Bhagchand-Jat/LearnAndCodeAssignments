package com.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class CartItemTest {

    @Test
    void testConstructorAndGetters() {
        CartItem cartItem = new CartItem(101L, 3, new BigDecimal("599.99"));

        assertThat(cartItem.getProductId()).isEqualTo(101L);
        assertThat(cartItem.getQuantity()).isEqualTo(3);
        assertThat(cartItem.getPrice()).isEqualByComparingTo("599.99");
    }

    @Test
    void testSetters() {
        CartItem cartItem = new CartItem(null, 0, BigDecimal.ZERO);
        cartItem.setProductId(202L);
        cartItem.setQuantity(5);
        cartItem.setPrice(new BigDecimal("199.50"));

        assertThat(cartItem.getProductId()).isEqualTo(202L);
        assertThat(cartItem.getQuantity()).isEqualTo(5);
        assertThat(cartItem.getPrice()).isEqualByComparingTo("199.50");
    }

    @Test
    void testToString() {
        CartItem cartItem = new CartItem(303L, 2, new BigDecimal("99.99"));
        String result = cartItem.toString();

        assertThat(result).contains("productId=303");
        assertThat(result).contains("quantity=2");
        assertThat(result).contains("price=99.99");
    }

    @Test
    void testFormatCartItemDetails() {
        CartItem cartItem = new CartItem(404L, 1, new BigDecimal("1499.00"));
        Locale.setDefault(new Locale("en", "IN")); 

        String formatted = cartItem.formatCartItemDetails();

        assertThat(formatted).contains("Product ID: 404");
        assertThat(formatted).contains("Quantity: 1");
        assertThat(formatted).contains("₹1,499.00");
    }
}
