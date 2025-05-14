package com.ecommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    void testProductConstructorAndGetters() {
        Category category = new Category(1L, "Electronics");
        Product product = new Product(101L, "Phone", new BigDecimal("15000.00"), category);

        assertThat(product.getId()).isEqualTo(101L);
        assertThat(product.getName()).isEqualTo("Phone");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("15000.00"));
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    void testProductSetters() {
        Product product = new Product();
        Category category = new Category(2L, "Books");

        product.setId(102L);
        product.setName("Java Book");
        product.setPrice(new BigDecimal("499.99"));
        product.setCategory(category);

        assertThat(product.getId()).isEqualTo(102L);
        assertThat(product.getName()).isEqualTo("Java Book");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("499.99"));
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    void testFormatProductDetails() {
        Category category = new Category(3L, "Clothing");
        Product product = new Product(103L, "Shirt", new BigDecimal("799.00"), category);

        String formatted = product.formatProductDetails();

        assertThat(formatted).contains("Product ID: 103");
        assertThat(formatted).contains("Name: Shirt");
        assertThat(formatted).contains("₹"); 
        assertThat(formatted).contains("Category: Clothing");
    }

    @Test
    void testFormatProductDetailsWithNulls() {
        Product product = new Product();
        String formatted = product.formatProductDetails();

        assertThat(formatted).contains("Product ID: 0");
        assertThat(formatted).contains("Name: N/A");
        assertThat(formatted).contains("₹0.00");
        assertThat(formatted).contains("Category: N/A");
    }

    @Test
    void testToString() {
        Category category = new Category(4L, "Sports");
        Product product = new Product(104L, "Football", new BigDecimal("499.50"), category);

        String expected = "Product [id=104, name=Football, price=499.50, category=" + category.toString() + "]";
        assertThat(product.toString()).isEqualTo(expected);
    }
}
