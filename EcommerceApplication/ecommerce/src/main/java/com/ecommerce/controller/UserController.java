package com.ecommerce.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/signup")
	public ResponseEntity<User> signUp(@RequestBody User user) {
		user.setId(System.currentTimeMillis());
		if (userRepository.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return new ResponseEntity<>(userRepository.save(user),HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
		Optional<User> user = userRepository.findByEmail(email);
		
		if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())){
			return new ResponseEntity<>(  user.get(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
