package com.ecommerce.repository;

import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindByEmail() {

        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("secret");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("alice@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("Alice", foundUser.get().getName());
        assertEquals("alice@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testExistsByEmailReturnsTrue() {

        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setPassword("123456");

        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("bob@example.com"));
    }

    @Test
    public void testExistsByEmailReturnsFalse() {

        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    public void testFindByEmailReturnsEmptyIfNotFound() {
        Optional<User> result = userRepository.findByEmail("doesnotexist@example.com");
        assertFalse(result.isPresent());
    }
}
