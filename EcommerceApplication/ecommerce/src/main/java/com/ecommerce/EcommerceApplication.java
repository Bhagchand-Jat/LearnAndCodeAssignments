package com.ecommerce;

import com.ecommerce.handler.AuthenticationHandler;
import com.ecommerce.handler.CartHandler;
import com.ecommerce.handler.OrderHandler;
import com.ecommerce.handler.ProductHandler;
import com.ecommerce.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {

    public static final String BASE_URL = "http://localhost:8080/api";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);
    private static Optional<User> user = Optional.empty();

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(restTemplate, scanner);

        try (scanner) {
            boolean isRunning = true;
            while (isRunning) {
                displayMainMenu();
                int choice = getUserInput();

                switch (choice) {
                    case 1 -> authenticationHandler.signUp();
                    case 2 -> user = authenticationHandler.login();
                    case 3 -> exitApplication();
                    default -> System.out.println("Invalid choice, try again!");
                }

                while (user.isPresent()) {
                    handleLoggedInUser(authenticationHandler);
                }
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n1. Sign Up\n2. Login\n3. Exit");
        System.out.print("Enter Your Choice: ");
    }

    private int getUserInput() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void exitApplication() {
        System.out.println("Exiting Application");
        System.exit(0);
    }

    private void handleLoggedInUser(AuthenticationHandler authenticationHandler) {
        CartHandler cartHandler = new CartHandler(restTemplate, user.get().getId(), scanner);
        OrderHandler orderHandler = new OrderHandler(restTemplate, user.get().getId(), scanner);
        ProductHandler productHandler = new ProductHandler(restTemplate, scanner);

        displayUserMenu();
        int userChoice = getUserInput();

        switch (userChoice) {
            case 1 -> productHandler.displayCategoryAndHandleSelection();
            case 2 -> productHandler.viewProducts();
            case 3 -> orderHandler.placeOrder();
            case 4 -> orderHandler.viewOrderHistory();
            case 5 -> cartHandler.manageCart();
            case 6 -> user = authenticationHandler.logout();
            default -> System.out.println("Invalid choice, try again!");
        }
    }

    private void displayUserMenu() {
        System.out.println("\n1. View Categories");
        System.out.println("2. View Products");
        System.out.println("3. Place Order");
        System.out.println("4. View Order History");
        System.out.println("5. Manage Cart");
        System.out.println("6. Logout");
        System.out.print("Enter Your Choice: ");
    }
}
