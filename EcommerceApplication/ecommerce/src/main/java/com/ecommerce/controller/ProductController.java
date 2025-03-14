package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@GetMapping
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable Long productId) {
		Optional<Product> productOptional = productRepository.findById(productId);

		if (productOptional.isPresent()) {
			return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public Product addProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}
}
