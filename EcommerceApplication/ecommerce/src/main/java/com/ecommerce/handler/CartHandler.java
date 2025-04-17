package com.ecommerce.handler;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;

public class CartHandler {
    private final RestTemplate restTemplate;
    private final Scanner scanner;
    private final String BASE_URL;
    private final Long loggedInUserId;

    public CartHandler(RestTemplate restTemplate, String BASE_URL, Long loggedInUserId, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
        this.BASE_URL = BASE_URL;
        this.loggedInUserId = loggedInUserId;
    }

    public void addProductToCart() {
        System.out.print("Enter product ID: ");
        Long productId = scanner.nextLong();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = restTemplate.getForObject(BASE_URL + "/products/" + productId, Product.class);
        if (product != null) {
            CartItem item = new CartItem(productId, quantity, product.getPrice());

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

    public void viewCart() {
        try {
            Cart cart = restTemplate.getForObject(BASE_URL + "/cart/view/" + loggedInUserId, Cart.class);
            if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
                System.out.println("Your Cart:");
                for (CartItem item : cart.getItems()) {
                    System.out.println(item.formatCartItemDetails());
                }
                System.out.println("  Total Price: " + cart.getTotalPrice());
            } else {
                System.out.println("Your cart is empty.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing cart: " + e.getMessage());
        }
    }

    public void removeProductFromCart() {
        System.out.print("Enter product ID to remove: ");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        try {
            restTemplate.delete(BASE_URL + "/cart/delete?userId=" + loggedInUserId + "&productId=" + productId);
            System.out.println("Item removed from cart (if it existed).");
        } catch (Exception e) {
            System.out.println("Error removing from cart: " + e.getMessage());
        }
    }

    public void placeOrderFromCart() {
        try {
            Cart cart = restTemplate.getForObject(BASE_URL + "/cart/view/" + loggedInUserId, Cart.class);

            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                System.out.println("Your cart is empty. Add items before placing an order.");
                return;
            }

            BigDecimal totalPrice = cart.getTotalPrice();
            System.out.println("Total amount:" + totalPrice);

            System.out.print("Confirm order (y/n): ");
            String confirmation = scanner.nextLine();
            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Order cancelled.");
                return;
            }

            for (CartItem item : cart.getItems()) {
                for (int quantityIndex = 1; quantityIndex <= item.getQuantity(); quantityIndex++) {
                    try {
                        Order newOrder = restTemplate.postForObject(
                                BASE_URL + "/orders?userId=" + loggedInUserId + "&productId=" + item.getProductId(),
                                null, Order.class);
                        if (newOrder != null) {
                            System.out.println("Order placed for product ID " + item.getProductId() + ": "
                                    + newOrder.formatOrderDetails());
                        }
                    } catch (HttpClientErrorException e) {
                        System.out.println(
                                "Error placing order for product ID " + item.getProductId() + ": " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println(
                                "Error placing order for product ID " + item.getProductId() + ": " + e.getMessage());
                    }
                }

            }

            clearCart();

            System.out.println("Order placed successfully for all items in cart!");

        } catch (HttpClientErrorException e) {
            System.out.println("Error retrieving cart: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error placing order from cart: " + e.getMessage());
        }
    }

    public void clearCart() {
        try {
            restTemplate.delete(BASE_URL + "/cart/clear/" + loggedInUserId);
            System.out.println("Cart cleared successfully!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }

    
    public void manageCart() {
        System.out.println(
                "\n1. Add Product to Cart\n2. View Cart\n3. Remove Product from Cart\n4. Place Order from Cart\n5. Back to Main Menu");
        System.out.print("Enter Your Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addProductToCart();
            case 2 -> viewCart();
            case 3 -> removeProductFromCart();
            case 4 -> placeOrderFromCart();
            case 5 -> System.out.println();
            default -> System.out.println("Invalid choice!");
        }
        if (choice != 5) {
            manageCart();
        }

    }

}
