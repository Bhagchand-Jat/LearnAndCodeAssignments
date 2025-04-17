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

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);
    private static Optional<User> user=Optional.empty();

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(restTemplate, BASE_URL, scanner);

        try (scanner) {
            boolean isRunning = true;

            while (isRunning) {
                System.out.println("\n1. Sign Up\n2. Login\n3. Exit");
                System.out.print("Enter Your Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> authenticationHandler.signUp();
                    case 2 -> {
                        user = authenticationHandler.login();

                    }
                    case 3 -> {
                        isRunning = false;
                        System.out.println("Exiting Application");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice, try again!");
                }

                while (user.isPresent()) {
                    CartHandler cartHandler = new CartHandler(restTemplate, BASE_URL, user.get().getId(), scanner);
                    OrderHandler orderHandler = new OrderHandler(restTemplate, BASE_URL, user.get().getId(), scanner);
                    ProductHandler productHandler = new ProductHandler(restTemplate, BASE_URL, scanner);

                    System.out.println(
                            "\n1. View Categories\n2. View Products\n3. Place Order\n4. View Order History\n5. Manage Cart\n6. Logout");
                    System.out.print("Enter Your Choice: ");
                    int userChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (userChoice) {
                        case 1 -> productHandler.viewCategories();
                        case 2 -> productHandler.viewProducts();
                        case 3 -> orderHandler.placeOrder();
                        case 4 -> orderHandler.viewOrderHistory();
                        case 5 -> cartHandler.manageCart();
                        case 6 -> {
                            user = authenticationHandler.logout();
                        }
                        default -> System.out.println("Invalid choice, try again!");
                    }
                }
            }
        }
    }

}
