package com.ecommerce.handler;

import java.util.Scanner;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.EcommerceApplication;
import com.ecommerce.model.Order;

public class OrderHandler {

    private final RestTemplate restTemplate;
    private final Scanner scanner;
    private final Long loggedInUserId;

    public OrderHandler(RestTemplate restTemplate,Long loggedInUserId, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
        this.loggedInUserId = loggedInUserId;
    }

    public void placeOrder() {
        Long productId = promptForProductId();
        submitOrderRequest(productId);
    }

    private Long promptForProductId() {
        System.out.print("Enter product ID to order: ");
        Long productId = scanner.nextLong();
        scanner.nextLine(); 
        return productId;
    }

    private void submitOrderRequest(Long productId) {
        try {
            Order newOrder = restTemplate.postForObject(
                EcommerceApplication.BASE_URL + "/orders?userId=" + loggedInUserId + "&productId=" + productId,
                null,
                Order.class
            );

            if (newOrder != null) {
                System.out.println("Order placed successfully.\n" + newOrder.formatOrderDetails());
            }

        } catch (HttpClientErrorException e) {
            handleOrderError(e, productId);
        } catch (Exception e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    private void handleOrderError(HttpClientErrorException e, Long productId) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            System.out.println("Error: Product with ID " + productId + " not found.");
        } else {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    public void viewOrderHistory() {
        try {
            Order[] orders = fetchOrderHistory();
            if (orders != null && orders.length > 0) {
                System.out.println("Order History:");
                for (Order order : orders) {
                    System.out.println(order.formatOrderDetails());
                }
            } else {
                System.out.println("No order history found.");
            }

        } catch (HttpClientErrorException e) {
            handleOrderHistoryError(e);
        } catch (Exception e) {
            System.out.println("Error retrieving order history: " + e.getMessage());
        }
    }

    private Order[] fetchOrderHistory() {
        return restTemplate.getForObject(EcommerceApplication.BASE_URL + "/orders/" + loggedInUserId, Order[].class);
    }

    private void handleOrderHistoryError(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            System.out.println("No order history found for user ID: " + loggedInUserId);
        } else {
            System.out.println("Error retrieving order history: " + e.getMessage());
        }
    }
}
