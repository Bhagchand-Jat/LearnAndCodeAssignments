package com.ecommerce.handler;

import java.util.Scanner;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.model.Order;

public class OrderHandler {
    private final RestTemplate restTemplate;
    private final Scanner scanner;
    private final String BASE_URL;
    private final Long loggedInUserId;
    public OrderHandler(RestTemplate restTemplate, String BASE_URL, Long loggedInUserId, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
        this.BASE_URL = BASE_URL;
        this.loggedInUserId = loggedInUserId;
    }

    
    public  void placeOrder() {
        System.out.print("Enter product ID to order: ");
        Long productId = scanner.nextLong();
        scanner.nextLine();

        try {
            Order newOrder = restTemplate.postForObject(
                    BASE_URL + "/orders?userId=" + loggedInUserId + "&productId=" + productId, null, Order.class);
            if (newOrder != null) {
                System.out.println("Order placed successfully. \n" + newOrder.formatOrderDetails());
            }
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

    public void viewOrderHistory() {
        try {
            Order[] orders = restTemplate.getForObject(BASE_URL + "/orders/" + loggedInUserId, Order[].class);
            if (orders != null && orders.length > 0) {
                System.out.println("Order History:");
                for (Order order : orders) {
                    System.out.println(order.formatOrderDetails());
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
}
