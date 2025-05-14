package com.ecommerce.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {

    @Test
    void testNoArgsConstructor() {
        Category category = new Category();
        assertThat(category).isNotNull();
        assertThat(category.getId()).isNull();
        assertThat(category.getName()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        Category category = new Category(1L, "Electronics");
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("Electronics");
    }

    @Test
    void testSettersAndGetters() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Books");

        assertThat(category.getId()).isEqualTo(2L);
        assertThat(category.getName()).isEqualTo("Books");
    }

    @Test
    void testToString() {
        Category category = new Category(3L, "Furniture");
        String toStringOutput = category.toString();

        assertThat(toStringOutput).contains("Category");
        assertThat(toStringOutput).contains("id=3");
        assertThat(toStringOutput).contains("name=Furniture");
    }
}
