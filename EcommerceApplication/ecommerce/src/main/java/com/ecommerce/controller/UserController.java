package com.ecommerce.controller;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CartController cartController;
	private final Logger logger = Logger.getLogger(UserController.class.getName());
	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserController(CartController cartController) {
        this.cartController = cartController;
    }

	@PostMapping("/signup")
	public ResponseEntity<User> signUp(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return new ResponseEntity<>(userRepository.save(user),HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
		Optional<User> user = userRepository.findByEmail(email);
		if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())){
			return new ResponseEntity<>(  "Login successful",HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid credentials",HttpStatus.NOT_FOUND);
	}

	@GetMapping
	public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
