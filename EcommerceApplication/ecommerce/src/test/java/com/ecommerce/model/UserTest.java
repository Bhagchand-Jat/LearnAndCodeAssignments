package com.ecommerce.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User(1L, "john@example.com", "John", "password123");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    void testUserSetters() {
        User user = new User();
        user.setId(2L);
        user.setEmail("jane@example.com");
        user.setName("Jane");
        user.setPassword("securePass");

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getName()).isEqualTo("Jane");
        assertThat(user.getPassword()).isEqualTo("securePass");
    }

    @Test
    void testToString() {
        User user = new User(3L, "alice@example.com", "Alice", "12345");
        String expected = "User [id=3, email=alice@example.com, name=Alice, password=12345]";
        assertThat(user.toString()).isEqualTo(expected);
    }
}
