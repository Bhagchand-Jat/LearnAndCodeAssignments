package com.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    void testOrderConstructorAndGetters() {
        User user = new User(1L, "test@example.com", "Test User", "password");
        Product product = new Product(2L, "Headphones", new BigDecimal("1999.00"), new Category(3L, "Electronics"));
        LocalDateTime deliveryDate = LocalDateTime.now().plusDays(3);

        Order order = new Order(new BigDecimal("1999.00"), deliveryDate, user, product);

        assertThat(order.getPrice()).isEqualTo(new BigDecimal("1999.00"));
        assertThat(order.getOrderDate()).isNotNull();
        assertThat(order.getExpectedDelivery()).isEqualTo(deliveryDate);
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getProduct()).isEqualTo(product);
    }

    @Test
    void testSetExpectedDelivery() {
        Order order = new Order();
        LocalDateTime newDeliveryDate = LocalDateTime.now().plusDays(5);

        order.setExpectedDelivery(newDeliveryDate);
        assertThat(order.getExpectedDelivery()).isEqualTo(newDeliveryDate);
    }

    @Test
    void testSetPrice() {
        Order order = new Order();
        BigDecimal newPrice = new BigDecimal("1499.50");

        order.setPrice(newPrice);
        assertThat(order.getPrice()).isEqualTo(newPrice);
    }

    @Test
    void testFormatOrderDetails() {
        User user = new User(1L, "user@example.com", "User Name", "password");
        Product product = new Product(2L, "Laptop", new BigDecimal("50000.00"), new Category(3L, "Electronics"));
        LocalDateTime deliveryDate = LocalDateTime.of(2025, 5, 20, 10, 0, 0);

        Order order = new Order(new BigDecimal("50000.00"), deliveryDate, user, product);

        String result = order.formatOrderDetails();

        assertThat(result).contains("Product Name: Laptop");
        assertThat(result).contains("₹");
        assertThat(result).contains("Expected Delivery: 2025-05-20 10:00:00");
        assertThat(result).contains("Order Date:");
    }

    @Test
    void testFormatOrderDetailsWithNulls() {
        Order order = new Order();

        String formatted = order.formatOrderDetails();

        assertThat(formatted).contains("Product Name: N/A");
        assertThat(formatted).contains("₹0.00");
        assertThat(formatted).contains("Order Date: N/A");
        assertThat(formatted).contains("Expected Delivery: N/A");
    }

    @Test
    void testToString() {
        User user = new User(1L, "user@example.com", "User Name", "pass123");
        Product product = new Product(2L, "TV", new BigDecimal("30000.00"), new Category(4L, "Appliances"));
        LocalDateTime deliveryDate = LocalDateTime.of(2025, 6, 1, 14, 0);
        Order order = new Order(new BigDecimal("30000.00"), deliveryDate, user, product);

        String toString = order.toString();

        assertThat(toString).contains("Order [id=");
        assertThat(toString).contains("user=" + user.toString());
        assertThat(toString).contains("product=" + product.toString());
        assertThat(toString).contains("price=30000.00");
        assertThat(toString).contains("expectedDelivery=2025-06-01T14:00");
    }
}
