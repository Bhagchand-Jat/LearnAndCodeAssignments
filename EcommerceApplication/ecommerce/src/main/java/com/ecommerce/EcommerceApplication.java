package com.ecommerce;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Category;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {

    private static final String BASE_URL = "http://localhost:8080/api"; 
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);
    private static Long loggedInUserId = null;
    private static String loggedInUserName = null;

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

        String email;
        boolean validEmail = false;
        do {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                validEmail = true;
            } else {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        } while (!validEmail);

        String password;
        boolean validPassword = false;
        do {
            System.out.print("Enter password (at least 8 characters): ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                validPassword = true;
            } else {
                System.out.println("Invalid password. Must be at least 8 characters.");
            }
        } while (!validPassword);

        User newUser = new User(System.currentTimeMillis(), email, name, password);
        User response = restTemplate.postForObject(BASE_URL + "/users/signup", newUser, User.class);
        System.out.println("User registered: " + response);
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }


    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            String loginResponse = restTemplate
                    .postForObject(BASE_URL + "/users/login?email=" + email + "&password=" + password, null, String.class);

            if ("Login successful".equals(loginResponse)) {
                setSecurityContext(email);
                System.out.println("Login successful!");
                User user = restTemplate.getForObject(BASE_URL + "/users?email=" + email, User.class);

                if (user != null && user.getId() != null) {
                    loggedInUserId = user.getId();
                    loggedInUserName = user.getName();
                    System.out.println("Welcome, " + loggedInUserName + "!");
                } else {
                    System.out.println("Error: User not found or invalid response.");
                    loggedInUserId = null;
                    loggedInUserName = null;
                }
            } else {
                System.out.println("Invalid credentials!");
            }
        }  catch (HttpClientErrorException e) {
            System.out.println("Login failed: " + e.getMessage());  // More specific error
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during login: " + e.getMessage());
        }
    }

    private static void showUserMenu() {
        System.out.println("\n1. View Categories\n2. View Products\n3. Place Order\n4. View Order History\n5. Manage Cart\n6. Logout");
        int userChoice = scanner.nextInt();
        scanner.nextLine();

        switch (userChoice) {
            case 1 -> viewCategories();
            case 2 -> viewProducts();
            case 3 -> placeOrder();
            case 4 -> viewOrderHistory();
            case 5 -> manageCart();
            case 6 -> {
                loggedInUserId = null;
                loggedInUserName = null;
                System.out.println("Logged out successfully!");
            }
            default -> System.out.println("Invalid choice, try again!");
        }
    }

    private static void viewCategories() {
        Category[] categories = restTemplate.getForObject(BASE_URL + "/categories", Category[].class);

        if (categories == null || categories.length == 0) {
            System.out.println("No categories found. Please add categories first.");
            return;
        }

        System.out.println("Available Categories:");
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getName());
        }
        System.out.println("0. Go Back");

        System.out.print("Enter the number of the category to view products: ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();

        if (categoryChoice > 0 && categoryChoice <= categories.length) {
            Category selectedCategory = categories[categoryChoice - 1];
            viewProductsByCategory(selectedCategory);
        } else if (categoryChoice == 0) {
            showUserMenu();
        } else {
            System.out.println("Invalid category choice.");
        }
    }

    private static void viewProducts() {
        List<Product> products = Arrays.asList(restTemplate.getForObject(BASE_URL + "/products", Product[].class));
        System.out.println("Products: ");
        if(products != null && !products.isEmpty()){
            for (Product product : products) {
                System.out.println(formatProduct(product));
            }
        }else{
            System.out.println("No Products found");
        }

    }

    private static void viewProductsByCategory(Category selectedCategory) {
        Product[] products = restTemplate.getForObject(BASE_URL + "/products", Product[].class);

        if (products == null || products.length == 0) {
            System.out.println("No products found.");
            return;
        }

        List<Product> filteredProducts = Arrays.stream(products)
            .filter(product -> product.getCategory() != null && product.getCategory().getId() != null && Objects.equals(product.getCategory().getId(), selectedCategory.getId()))
            .collect(Collectors.toList());


        if (filteredProducts.isEmpty()) {
            System.out.println("No products found in the " + selectedCategory.getName() + " category.");
        } else {
            System.out.println("Products in the " + selectedCategory.getName() + " category:");
            for (Product product : filteredProducts) {
                System.out.println(formatProduct(product));
            }
        }
    }

    private static String formatProduct(Product product) {
        return "Product ID: " + product.getId() +
                ", Name: " + product.getName() +
                ", Price: " + product.getPrice() +
                ", Category: " + (product.getCategory() != null ? product.getCategory().getName() : "N/A");
    }


    private static void placeOrder() {
        System.out.print("Enter product ID to order: ");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        try {
            Order newOrder = restTemplate.postForObject(
                    BASE_URL + "/orders/?userId=" + loggedInUserId + "&productId=" + productId, null, Order.class);
            System.out.println("Order placed: " + newOrder);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("Error: Product with ID " + productId + " not found.");
            } else {
                System.out.println("Error placing order: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    private static void viewOrderHistory() {
        try {
            Order[] orders = restTemplate.getForObject(BASE_URL + "/orders/" + loggedInUserId, Order[].class);
            if (orders != null && orders.length > 0) {
                System.out.println("Order History:");
                for (Order order : orders) {
                    System.out.println(formatOrder(order));
                }
            } else {
                System.out.println("No order history found.");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("No order history found for user ID: " + loggedInUserId);
            } else {
                System.out.println("Error retrieving order history: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error retrieving order history: " + e.getMessage());
        }
    }

    private static String formatOrder(Order order) {
        return "Order ID: " + order.getId() +
               ", Product: " + order.getProduct().getName() +
               ", Price: " + order.getPrice() +
               ", Order Date: " + order.getOrderDate();
    }

    // --------------- Cart Operations ---------------

    private static void manageCart() {
        System.out.println("\n1. Add to Cart\n2. View Cart\n3. Remove from Cart\n4. Place Order from Cart\n5. Back to Main Menu");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addToCart();
            case 2 -> viewCart();
            case 3 -> removeFromCart();
            case 4 -> placeOrderFromCart(); // New case
            case 5 -> showUserMenu(); // Back to main menu
            default -> System.out.println("Invalid choice!");
        }
    }

    private static void addToCart() {
        System.out.print("Enter product ID: ");
        Long productId = scanner.nextLong();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = restTemplate.getForObject(BASE_URL + "/products/" + productId, Product.class);
        if (product != null) {
            CartItem item = new CartItem();
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());

            try {
                String response = restTemplate.postForObject(
                        BASE_URL + "/cart/add?userId=" + loggedInUserId, item, String.class);
                System.out.println(response);
            } catch (Exception e) {
                System.out.println("Error adding to cart: " + e.getMessage());
            }
        } else {
            System.out.println("Product not found!");
        }
    }

    private static void viewCart() {
        try {
            Cart cart = restTemplate.getForObject(BASE_URL + "/cart/view/" + loggedInUserId, Cart.class);
            if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
                System.out.println("Your Cart:");
                for (CartItem item : cart.getItems()) {
                    System.out.println("  Product ID: " + item.getProductId() +
                                       ", Quantity: " + item.getQuantity() +
                                       ", Price: " + item.getPrice());
                }
                System.out.println("  Total Price: " + cart.getTotalPrice());
            } else {
                System.out.println("Your cart is empty.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing cart: " + e.getMessage());
        }
    }

    private static void removeFromCart() {
        System.out.print("Enter product ID to remove: ");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        try {
            restTemplate.delete(BASE_URL + "/cart/remove?userId=" + loggedInUserId + "&productId=" + productId);
            System.out.println("Item removed from cart (if it existed).");
        } catch (Exception e) {
            System.out.println("Error removing from cart: " + e.getMessage());
        }
    }

    private static void placeOrderFromCart() {
        try {
            Cart cart = restTemplate.getForObject(BASE_URL + "/cart/view/" + loggedInUserId, Cart.class);

            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                System.out.println("Your cart is empty. Add items before placing an order.");
                return;
            }

              BigDecimal totalPrice = cart.getTotalPrice();
            System.out.println("Total amount:" +totalPrice);

            System.out.print("Confirm order (y/n): ");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Order cancelled.");
                return;
            }

            for (CartItem item : cart.getItems()) {
                try {
                    Order newOrder = restTemplate.postForObject(
                            BASE_URL + "/orders?userId=" + loggedInUserId + "&productId=" + item.getProductId(), null, Order.class);
                    System.out.println("Order placed for product ID " + item.getProductId() + ": " + newOrder);
                } catch (HttpClientErrorException e) {
                    System.out.println("Error placing order for product ID " + item.getProductId() + ": " + e.getMessage());
                    // Consider whether to continue placing orders for other items if one fails
                } catch (Exception e) {
                    System.out.println("Error placing order for product ID " + item.getProductId() + ": " + e.getMessage());
                }
            }

            // Clear the cart after placing the order
            clearCart();

            System.out.println("Order placed successfully for all items in cart!");

        } catch (HttpClientErrorException e) {
            System.out.println("Error retrieving cart: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error placing order from cart: " + e.getMessage());
        }
    }

     private static void clearCart() {
        try {
            restTemplate.delete(BASE_URL + "/cart/clear/" + loggedInUserId);
            System.out.println("Cart cleared successfully!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
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
