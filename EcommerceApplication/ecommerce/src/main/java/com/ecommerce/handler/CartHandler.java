package com.ecommerce.handler;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.web.client.RestTemplate;

import com.ecommerce.EcommerceApplication;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;

public class CartHandler {
    private final RestTemplate restTemplate;
    private final Scanner scanner;
    private final Long loggedInUserId;

    public CartHandler(RestTemplate restTemplate, Long loggedInUserId, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
        this.loggedInUserId = loggedInUserId;
    }

    public void manageCart() {
        while (true) {
            displayCartMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1 -> addProductToCart();
                case 2 -> viewCart();
                case 3 -> removeProductFromCart();
                case 4 -> placeOrderFromCart();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void displayCartMenu() {
        System.out.println(
                "\n1. Add Product to Cart\n2. View Cart\n3. Remove Product from Cart\n4. Place Order from Cart\n5. Back to Main Menu");
        System.out.print("Enter Your Choice: ");
    }

    private int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void addProductToCart() {
        Long productId = promptProductId();
        int quantity = promptQuantity();

        Optional<Product> productOpt = fetchProduct(productId);
        if (productOpt.isEmpty()) {
            System.out.println("Product not found!");
            return;
        }

        Product product = productOpt.get();
        CartItem item = new CartItem(productId, quantity, product.getPrice());

        try {
            String response = restTemplate.postForObject(
                    EcommerceApplication.BASE_URL + "/cart/add?userId=" + loggedInUserId, item, String.class);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error adding to cart: " + e.getMessage());
        }
    }

    private void viewCart() {
        try {
            Optional<Cart> cartOpt = fetchCart();
            if (cartOpt.isPresent() && !cartOpt.get().getItems().isEmpty()) {
                displayCart(cartOpt.get());
            } else {
                System.out.println("Your cart is empty.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing cart: " + e.getMessage());
        }
    }

    private void removeProductFromCart() {
        Long productId = promptProductId();
        try {
            restTemplate.delete(EcommerceApplication.BASE_URL + "/cart/delete?userId=" + loggedInUserId + "&productId=" + productId);
            System.out.println("Item removed from cart (if it existed).");
        } catch (Exception e) {
            System.out.println("Error removing from cart: " + e.getMessage());
        }
    }

    private void placeOrderFromCart() {
        try {
            Optional<Cart> cartOpt = fetchCart();
            if (cartOpt.isEmpty() || cartOpt.get().getItems().isEmpty()) {
                System.out.println("Your cart is empty. Add items before placing an order.");
                return;
            }

            Cart cart = cartOpt.get();

            if (!confirmOrder(cart.getTotalPrice())) return;

            placeOrdersForCartItems(cart);
            clearCart();
            System.out.println("Order placed successfully for all items in cart!");

        } catch (Exception e) {
            System.out.println("Error placing order from cart: " + e.getMessage());
        }
    }

    public void clearCart() {
        try {
            restTemplate.delete(EcommerceApplication.BASE_URL + "/cart/clear/" + loggedInUserId);
            System.out.println("Cart cleared successfully!");
        } catch (Exception e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }


    private Long promptProductId() {
        System.out.print("Enter product ID: ");
        return scanner.nextLong();
    }

    private int promptQuantity() {
        System.out.print("Enter quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine(); 
        return qty;
    }

    private Optional<Product> fetchProduct(Long productId) {
        try {
            Product product = restTemplate.getForObject(EcommerceApplication.BASE_URL + "/products/" + productId, Product.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Cart> fetchCart() {
        try {
            Cart cart = restTemplate.getForObject(EcommerceApplication.BASE_URL + "/cart/view/" + loggedInUserId, Cart.class);
            return Optional.ofNullable(cart);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void displayCart(Cart cart) {
        System.out.println("Your Cart:");
        for (CartItem item : cart.getItems()) {
            System.out.println(item.formatCartItemDetails());
        }
        System.out.println("  Total Price: " + cart.getTotalPrice());
    }

    private boolean confirmOrder(BigDecimal totalPrice) {
        System.out.println("Total amount: " + totalPrice);
        System.out.print("Confirm order (y/n): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Order cancelled.");
            return false;
        }
        return true;
    }

    private void placeOrdersForCartItems(Cart cart) {
        for (CartItem item : cart.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                try {
                    Order order = restTemplate.postForObject(
                            EcommerceApplication.BASE_URL + "/orders?userId=" + loggedInUserId + "&productId=" + item.getProductId(),
                            null, Order.class);
                    Optional.ofNullable(order).ifPresent(o -> System.out.println("Order placed for product ID " +
                            item.getProductId() + ": " + o.formatOrderDetails()));
                } catch (Exception e) {
                    System.out.println("Error placing order for product ID " + item.getProductId() + ": " + e.getMessage());
                }
            }
        }
    }
}
