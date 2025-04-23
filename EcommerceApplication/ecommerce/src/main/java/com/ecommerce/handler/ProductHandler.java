package com.ecommerce.handler;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

import com.ecommerce.EcommerceApplication;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;

public class ProductHandler {
    private final RestTemplate restTemplate;
    private final Scanner scanner;

    public ProductHandler(RestTemplate restTemplate, Scanner scanner) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
    }

    public void displayCategoryAndHandleSelection() {
        List<Category> categories = getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found. Please add categories first.");
            return;
        }
    
        printCategoryList(categories);
    
        int categoryChoice = getCategoryChoice(categories.size());
    
        if (categoryChoice == 0) return;
    
        Category selectedCategory = categories.get(categoryChoice - 1);
        viewProductsByCategory(selectedCategory);
    }
    

    public void viewProducts() {
        List<Product> products = getAllProducts();

        System.out.println("Products:");
        if (products.isEmpty()) {
            System.out.println("No Products found.");
        } else {
            products.forEach(product -> System.out.println(product.formatProductDetails()));
        }
    }

    private void viewProductsByCategory(Category selectedCategory) {
        List<Product> allProducts = getAllProducts();
        List<Product> filtered = filterProductsByCategory(allProducts, selectedCategory);

        if (filtered.isEmpty()) {
            System.out.println("No products found in the " + selectedCategory.getName() + " category.");
        } else {
            System.out.println("Products in the " + selectedCategory.getName() + " category:");
            filtered.forEach(product -> System.out.println(product.formatProductDetails()));
        }
    }

    private List<Category> getAllCategories() {
        Category[] categories = restTemplate.getForObject(EcommerceApplication.BASE_URL + "/categories", Category[].class);
        return categories != null ? Arrays.asList(categories) : new ArrayList<>();
    }

    private List<Product> getAllProducts() {
        Product[] products = restTemplate.getForObject(EcommerceApplication.BASE_URL + "/products", Product[].class);
        return products != null ? Arrays.asList(products) : new ArrayList<>();
    }

    private List<Product> filterProductsByCategory(List<Product> products, Category category) {
        return products.stream()
            .filter(product -> product.getCategory() != null &&
                               Objects.equals(product.getCategory().getId(), category.getId()))
            .collect(Collectors.toList());
    }

    private void printCategoryList(List<Category> categories) {
        System.out.println("Available Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
        }
        System.out.println("0. Go Back");
    }

    private int getCategoryChoice(int maxOption) {
        System.out.print("Enter the number of the category to view products: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            if (choice >= 0 && choice <= maxOption) {
                return choice;
            } else {
                System.out.println("Invalid category choice.");
                return 0;
            }
        } catch (Exception e) {
            scanner.nextLine(); 
            System.out.println("Invalid input. Please enter a valid number.");
            return 0;
        }
    }

    public void addCategories() {
        List<Category> categories = List.of(
            new Category(null, "Electronics"),
            new Category(null, "Clothing"),
            new Category(null, "Books")
        );

        for (Category category : categories) {
            restTemplate.postForObject(EcommerceApplication.BASE_URL + "/categories", category, Category.class);
        }

        System.out.println("Dummy categories added successfully!");
    }

    public void addProducts() {
        List<Category> categories = getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found! Please add categories first.");
            return;
        }

        Map<String, Category> categoryMap = categories.stream()
            .collect(Collectors.toMap(Category::getName, c -> c));

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
                restTemplate.postForObject(EcommerceApplication.BASE_URL + "/products", product, Product.class);
            } else {
                System.out.println("Category not found for product: " + product.getName());
            }
        }

        System.out.println("Dummy products added successfully!");
    }

}
