package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal price;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDelivery;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Order() {
    }

    public Order(Long id, BigDecimal price, LocalDateTime orderDate, LocalDateTime expectedDelivery, User user,
            Product product) {
        this.id = id;
        this.price = price;
        this.orderDate = orderDate;
        this.expectedDelivery = expectedDelivery;
        this.user = user;
        this.product = product;
    }

    public Order(BigDecimal price, LocalDateTime orderDate, LocalDateTime expectedDelivery, User user,
            Product product) {
        this.price = price;
        this.orderDate = orderDate;
        this.expectedDelivery = expectedDelivery;
        this.user = user;
        this.product = product;
    }

    public Order(BigDecimal price, LocalDateTime orderDate, LocalDateTime expectedDelivery) {
        this.price = price;
        this.orderDate = orderDate;
        this.expectedDelivery = expectedDelivery;
    }

    public Order(Long id, BigDecimal price, LocalDateTime orderDate, LocalDateTime expectedDelivery) {
        this.id = id;
        this.price = price;
        this.orderDate = orderDate;
        this.expectedDelivery = expectedDelivery;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDateTime expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public String formatOrderDetails() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        currencyFormat.setCurrency(Currency.getInstance("INR"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Product Name: %s, Price: %s, Order Date: %s, Expected Delivery: %s",
                product != null ? product.getName() : "N/A",
                currencyFormat.format(price != null ? price : BigDecimal.ZERO),
                orderDate != null ? formatter.format(orderDate) : "N/A",
                expectedDelivery != null ? formatter.format(expectedDelivery) : "N/A");
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", user=" + user + ", product=" + product + ", price=" + price + ", orderDate="
                + orderDate + ", expectedDelivery=" + expectedDelivery + "]";
    }

}
