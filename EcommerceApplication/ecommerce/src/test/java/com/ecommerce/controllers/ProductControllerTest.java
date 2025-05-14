package com.ecommerce.controllers;

import com.ecommerce.controller.ProductController;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts_ReturnsStatusOk() {
        when(productRepository.findAll()).thenReturn(List.of());
        ResponseEntity<List<Product>> response = productController.getAllProducts();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProducts_ReturnsProductList() {
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);
        ResponseEntity<List<Product>> response = productController.getAllProducts();
        assertEquals(products, response.getBody());
    }

    @Test
    void testGetProduct_Found_StatusOk() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ResponseEntity<?> response = productController.getProduct(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetProduct_Found_ReturnsProduct() {
        Product product = new Product();
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        ResponseEntity<?> response = productController.getProduct(2L);
        assertEquals(product, response.getBody());
    }

    @Test
    void testGetProduct_NotFound_StatusNotFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = productController.getProduct(100L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetProduct_NotFound_Message() {
        when(productRepository.findById(200L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = productController.getProduct(200L);
        assertEquals("Product not found", response.getBody());
    }


    @Test
    void testAddProduct_StatusOk() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);
        ResponseEntity<Product> response = productController.addProduct(product);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testAddProduct_ReturnsSavedProduct() {
        Product product = new Product();
        product.setName("Test");
        product.setPrice(new BigDecimal("99.99"));
        when(productRepository.save(product)).thenReturn(product);
        ResponseEntity<Product> response = productController.addProduct(product);
        assertEquals(product, response.getBody());
    }
}
