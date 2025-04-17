package com.ecommerce.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.ecommerce.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationHandler {

    private final RestTemplate restTemplate;
    private final Scanner scanner;
    private final String BASE_URL;

    public AuthenticationHandler(RestTemplate restTemplate, String BASE_URL, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
        this.BASE_URL = BASE_URL;
    }

    public void signUp() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        String email;
        boolean isEmailValid = false;
        do {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                isEmailValid = true;
            } else {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        } while (!isEmailValid);

        String password;
        boolean isPasswordValid = false;
        do {
            System.out.print("Enter password (at least 8 characters): ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                isPasswordValid = true;
            } else {
                System.out.println("Invalid password. Must be at least 8 characters.");
            }
        } while (!isPasswordValid);

        User newUser = new User(System.currentTimeMillis(), email, name, password);
        try {
            ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL + "/users/signup", newUser, User.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("User registered successfully");
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                System.out.println("\nRegistration failed: Email is already in use. Please try a different email.");
            } else {
                System.out.println("Registration failed: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }

    // Define a regular expression (regex) pattern for validating email addresses.
    // The pattern checks for:
    // - One or more word characters, hyphens, or dots before the '@' symbol.
    // - A domain name consisting of one or more word characters or hyphens followed
    // by a dot.
    // - A top-level domain (TLD) that is 2 to 4 characters long (e.g., .com, .org,
    // .info).
    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public Optional<User> login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            ResponseEntity<User> loginResponse = restTemplate
                    .postForEntity(BASE_URL + "/users/login?email=" + email + "&password=" + password, null,
                            User.class);

            User user = loginResponse.getBody();

            System.out.println("Login successful!");

            System.out.println("Welcome, " + user.getName() + "!");

            setSecurityContext(email);
            return Optional.of(user);

        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("Invalid credentials!");
            } else {
                System.out.println("Login Failed: " + exception.getMessage());
            }

        } catch (Exception exception) {
            System.out.println("An unexpected error occurred during login: " + exception.getMessage());
        }

        return Optional.empty();
    }

    public Optional<User> logout() {
        setSecurityContext(null);
        System.out.println("Logged out successfully!");
        return Optional.empty();
    }

    private void setSecurityContext(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
