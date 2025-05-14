package com.ecommerce.handler;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductHandlerTest {

    private RestTemplate restTemplate;
    private Scanner scanner;
    private ProductHandler productHandler;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        scanner = mock(Scanner.class);
        productHandler = new ProductHandler(restTemplate, scanner);
    }

    @Test
    public void testViewProductsWhenProductsExist() {
        Product[] products = {
            new Product(1L, "Laptop", new BigDecimal("75000"), new Category(1L, "Electronics")),
            new Product(2L, "Shirt", new BigDecimal("800"), new Category(2L, "Clothing"))
        };

        when(restTemplate.getForObject(contains("/products"), eq(Product[].class))).thenReturn(products);

        assertDoesNotThrow(() -> productHandler.viewProducts());
    }

    @Test
    public void testViewProductsWhenNoProductsExist() {
        when(restTemplate.getForObject(contains("/products"), eq(Product[].class)))
            .thenReturn(new Product[0]);

        assertDoesNotThrow(() -> productHandler.viewProducts());
    }

    @Test
    public void testDisplayCategoryAndHandleSelectionWithValidChoice() {
        Category[] categories = {
            new Category(1L, "Electronics"),
            new Category(2L, "Clothing")
        };

        Product[] products = {
            new Product(1L, "Laptop", new BigDecimal("75000"), categories[0]),
            new Product(2L, "Shirt", new BigDecimal("800"), categories[1])
        };

        when(restTemplate.getForObject(contains("/categories"), eq(Category[].class))).thenReturn(categories);
        when(restTemplate.getForObject(contains("/products"), eq(Product[].class))).thenReturn(products);
        when(scanner.nextInt()).thenReturn(1); 
        when(scanner.nextLine()).thenReturn("invalid");


        assertDoesNotThrow(() -> productHandler.displayCategoryAndHandleSelection());
    }

    @Test
    public void testDisplayCategoryAndHandleSelectionWithInvalidInput() {
        Category[] categories = {
            new Category(1L, "Electronics")
        };

        when(restTemplate.getForObject(contains("/categories"), eq(Category[].class))).thenReturn(categories);
        when(scanner.nextInt()).thenThrow(new InputMismatchException());
        when(scanner.nextLine()).thenReturn("invalid");


        assertDoesNotThrow(() -> productHandler.displayCategoryAndHandleSelection());
    }

    @Test
    public void testDisplayCategoryAndHandleSelectionWithNoCategories() {
        when(restTemplate.getForObject(contains("/categories"), eq(Category[].class))).thenReturn(new Category[0]);

        assertDoesNotThrow(() -> productHandler.displayCategoryAndHandleSelection());
    }

    @Test
    public void testAddCategories() {
        assertDoesNotThrow(() -> productHandler.addCategories());
        verify(restTemplate, times(3))
            .postForObject(contains("/categories"), any(Category.class), eq(Category.class));
    }

    @Test
    public void testAddProductsWithValidCategories() {
        List<Category> categories = List.of(
            new Category(1L, "Electronics"),
            new Category(2L, "Clothing"),
            new Category(3L, "Books")
        );

        when(restTemplate.getForObject(contains("/categories"), eq(Category[].class)))
            .thenReturn(categories.toArray(new Category[0]));

        assertDoesNotThrow(() -> productHandler.addProducts());

        // Expecting 9 dummy products
        verify(restTemplate, times(9))
            .postForObject(contains("/products"), any(Product.class), eq(Product.class));
    }

    @Test
    public void testAddProductsWithMissingCategory() {
        // Simulate "Books" category is missing
        List<Category> categories = List.of(
            new Category(1L, "Electronics"),
            new Category(2L, "Clothing")
        );

        when(restTemplate.getForObject(contains("/categories"), eq(Category[].class)))
            .thenReturn(categories.toArray(new Category[0]));

        assertDoesNotThrow(() -> productHandler.addProducts());

        // 6 products will be added (3 Electronics + 3 Clothing); Books are skipped
        verify(restTemplate, times(6))
            .postForObject(contains("/products"), any(Product.class), eq(Product.class));
    }
}
