package com.ecommerce.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.EcommerceApplication;
import com.ecommerce.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AuthenticationHandler {

    private final RestTemplate restTemplate;
    private final Scanner scanner;

    public AuthenticationHandler(RestTemplate restTemplate, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
    }

    public void signUp() {
        String name = prompt("Enter name: ");
        String email = promptForValidEmail();
        String password = promptForValidPassword();

        User newUser = new User(System.currentTimeMillis(), email, name, password);
        registerUser(newUser);
    }

    public String promptForValidEmail() {
        String email;
        do {
            email = prompt("Enter Email: ");
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        } while (!isValidEmail(email));
        return email;
    }

    public String promptForValidPassword() {
        String password;
        do {
            password = prompt("Enter password (at least 8 characters): ");
            if (!isValidPassword(password)) {
                System.out.println("Invalid password. Must be at least 8 characters.");
            }
        } while (!isValidPassword(password));
        return password;
    }

    public void registerUser(User newUser) {
        try {
            ResponseEntity<User> response = restTemplate.postForEntity(EcommerceApplication.BASE_URL + "/users/signup", newUser, User.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("User registered successfully");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                System.out.println("\nRegistration failed: Email is already in use.");
            } else {
                System.out.println("Registration failed: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }

    public Optional<User> login() {
        String email = prompt("Enter email: ");
        String password = prompt("Enter password: ");

        return performLogin(email, password);
    }

    public Optional<User> performLogin(String email, String password) {
        try {
            ResponseEntity<User> response = restTemplate.postForEntity(
                    EcommerceApplication.BASE_URL + "/users/login?email=" + email + "&password=" + password,
                    null,
                    User.class
            );

            return Optional.ofNullable(response.getBody()).map(user -> {
                System.out.println("Login successful!\nWelcome, " + user.getName() + "!");
                setSecurityContext(email);
                return user;
            });

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("Invalid credentials!");
            } else {
                System.out.println("Login Failed: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Unexpected error during login: " + e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<User> logout() {
        setSecurityContext(null);
        System.out.println("Logged out successfully!");
        return Optional.empty();
    }

    public void setSecurityContext(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        if(username!=null){
            UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
        }else{
            context.setAuthentication(null);
            
        }
       
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
