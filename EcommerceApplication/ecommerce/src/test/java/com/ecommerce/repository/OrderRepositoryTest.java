package com.ecommerce.repository;

import com.ecommerce.model.Category;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveAndFindByUserId() {

        Order order1 = new Order(BigDecimal.valueOf(40), LocalDateTime.now(), new User(1L, null, null, null), new Product(100L, "Product A", BigDecimal.valueOf(20), new Category(10L, "cat")));
        Order order2= new Order(BigDecimal.valueOf(40), LocalDateTime.now(), new User(2L, null, null, null), new Product(100L, "Product B", BigDecimal.valueOf(20), new Category(10L, "cat")));
        Order order3= new Order(BigDecimal.valueOf(40), LocalDateTime.now(), new User(3L, null, null, null), new Product(100L, "Product C", BigDecimal.valueOf(20), new Category(10L, "cat")));

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        List<Order> user1Orders = orderRepository.findByUserId(1L);

        assertEquals(2, user1Orders.size());
        assertTrue(user1Orders.stream().allMatch(o -> o.getId().equals(1L)));

        List<Order> user2Orders = orderRepository.findByUserId(2L);
        assertEquals(1, user2Orders.size());
        assertEquals("Product C", user2Orders.get(0).getProduct().getName());

        List<Order> user3Orders = orderRepository.findByUserId(3L);
        assertTrue(user3Orders.isEmpty());
    }
}
