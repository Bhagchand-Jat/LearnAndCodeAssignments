package com.ecommerce.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	 @Autowired
	    private OrderRepository orderRepository;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private ProductRepository productRepository;

	    @PostMapping
	    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestParam Long productId) {
	        Optional<User> user = userRepository.findById(userId);
	        Optional<Product> product = productRepository.findById(productId);

	        if (user.isPresent()) {
	        	 if (product.isPresent()) {
	        		 Order order = new Order(product.get().getPrice(),LocalDateTime.now(), LocalDateTime.now().plusDays(5), user.get(), product.get());
	        		 return new ResponseEntity<>(orderRepository.save(order), HttpStatus.CREATED);
	        	 } else {
	        		 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        	 }
	          
	        }
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    @GetMapping("/{userId}")
	    public List<Order> getOrderHistory(@PathVariable Long userId) {
	        return orderRepository.findByUserId(userId);
	    }
}
