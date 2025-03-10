package com.ecommerce;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.model.Category;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);
    private static Long loggedInUserId = null;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Override
    public void run(String... args) {
       // addDummyCategories();
       // addDummyProducts();
        try (scanner) {
            boolean isRunning = true;

            while (isRunning) {
                System.out.println("\n1. Sign Up\n2. Login\n3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> signUp();
                    case 2 -> login();
                    case 3 -> {
                        isRunning = false;
                        System.out.println("Exiting Application");
                    }
                    default -> System.out.println("Invalid choice, try again!");
                }

                while (loggedInUserId != null) {
                    showUserMenu();
                }
            }
        }
    }

    private static void signUp() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(System.currentTimeMillis(),email, name, password);
        User response = restTemplate.postForObject(BASE_URL + "/users/signup", newUser, User.class);
        System.out.println("User registered: " + response);
    }

    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String loginResponse = restTemplate
                .postForObject(BASE_URL + "/users/login?email=" + email + "&password=" + password, null, String.class);

        if ("Login successful".equals(loginResponse)) {
            setSecurityContext(email);
            System.out.println("Login successful!");
            User user = restTemplate.getForObject(BASE_URL + "/users?email=" + email, User.class);

            if (user != null && user.getId() != null) {
                loggedInUserId = user.getId();
            } else {
                System.out.println("Error: User not found or invalid response.");
                loggedInUserId = null;
            }
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n1. View Categories\n2. View Products\n3. Place Order\n4. View Order History\n5. Logout");
        int userChoice = scanner.nextInt();
        scanner.nextLine();

        switch (userChoice) {
            case 1 -> viewCategories();
            case 2 -> viewProducts();
            case 3 -> placeOrder();
            case 4 -> viewOrderHistory();
            case 5 -> {
                loggedInUserId = null;
                System.out.println("Logged out successfully!");
            }
            default -> System.out.println("Invalid choice, try again!");
        }
    }

    private static void viewCategories() {
        List<?> categories = Arrays.asList(restTemplate.getForObject(BASE_URL + "/categories", Object[].class));
        System.out.println("Categories: " + categories);
    }

    private static void viewProducts() {
        List<Product> products = Arrays.asList(restTemplate.getForObject(BASE_URL + "/products", Product[].class));
        System.out.println("Products: " + products);
    }

    private static void placeOrder() {
        System.out.print("Enter product ID to order: ");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        Order newOrder = restTemplate.postForObject(
                BASE_URL + "/orders/?userId=" + loggedInUserId + "&productId=" + productId, null, Order.class);
        System.out.println("Order placed: " + newOrder);
    }

    private static void viewOrderHistory() {
        List<Order> orders = Arrays
                .asList(restTemplate.getForObject(BASE_URL + "/orders/" + loggedInUserId, Order[].class));
        System.out.println("Order History: " + orders);
    }

    private static void setSecurityContext(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void addDummyCategories() {
        Category electronics = new Category(null, "Electronics");
        Category clothing = new Category(null, "Clothing");
        Category books = new Category(null, "Books");
    
        List<Category> categories = List.of(electronics, clothing, books);
    
        for (Category category : categories) {
            restTemplate.postForObject(BASE_URL + "/categories", category, Category.class);
        }
        System.out.println("Dummy categories added successfully!");
    }
    
    private void addDummyProducts() {
        Category[] categories = restTemplate.getForObject(BASE_URL + "/categories", Category[].class);
    
        if (categories == null || categories.length == 0) {
            System.out.println("No categories found! Please add categories first.");
            return;
        }

        Map<String, Category> categoryMap = Arrays.stream(categories)
                .collect(Collectors.toMap(Category::getName, category -> category));

        List<Product> products = List.of(
                new Product(null, "Laptop", BigDecimal.valueOf(75000), categoryMap.get("Electronics")),
                new Product(null, "Smartphone", BigDecimal.valueOf(50000), categoryMap.get("Electronics")),
                new Product(null, "Headphones", BigDecimal.valueOf(3000), categoryMap.get("Electronics")),
    
                new Product(null, "T-Shirt", BigDecimal.valueOf(800), categoryMap.get("Clothing")),
                new Product(null, "Jeans", BigDecimal.valueOf(1500), categoryMap.get("Clothing")),
                new Product(null, "Jacket", BigDecimal.valueOf(3000), categoryMap.get("Clothing")),
    
                new Product(null, "Novel", BigDecimal.valueOf(500), categoryMap.get("Books")),
                new Product(null, "Science Textbook", BigDecimal.valueOf(1200), categoryMap.get("Books")),
                new Product(null, "History Guide", BigDecimal.valueOf(900), categoryMap.get("Books"))
        );
    
        for (Product product : products) {
            if (product.getCategory() != null) {
                restTemplate.postForObject(BASE_URL + "/products", product, Product.class);
            } else {
                System.out.println("Category not found for product: " + product.getName());
            }
        }
    
        System.out.println("Dummy products added successfully!");
    }    
}
