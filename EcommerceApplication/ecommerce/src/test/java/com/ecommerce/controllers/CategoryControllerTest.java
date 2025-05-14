package com.ecommerce.controllers;

import com.ecommerce.controller.CategoryController;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories_StatusOk() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        ResponseEntity<List<Category>> response = categoryController.getAllCategories();
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllCategories_ReturnsCategoryList() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);
        ResponseEntity<List<Category>> response = categoryController.getAllCategories();
        assertEquals(categories, response.getBody());
    }


    @Test
    void testCreateCategory_StatusCreated() {
        Category category = new Category();
        when(categoryRepository.save(category)).thenReturn(category);
        ResponseEntity<Category> response = categoryController.createCategory(category);
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateCategory_ReturnsSavedCategory() {
        Category category = new Category();
        category.setName("Electronics");
        when(categoryRepository.save(category)).thenReturn(category);
        ResponseEntity<Category> response = categoryController.createCategory(category);
        assertEquals(category, response.getBody());
    }
}
