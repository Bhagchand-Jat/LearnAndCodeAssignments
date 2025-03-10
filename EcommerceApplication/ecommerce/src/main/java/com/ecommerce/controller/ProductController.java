package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	    @PostMapping
	    public Product addProduct(@RequestBody Product product) {
	        return productRepository.save(product);
	    }
}
