package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final List<Cart> carts = new ArrayList<>();

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestBody CartItem newItem) {
        Cart cart = getOrCreateCart(userId);
        addItemToCart(cart, newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to cart successfully!");
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<Cart> viewCart(@PathVariable Long userId) {
        Optional<Cart> cart = findCartByUserId(userId);
        if (cart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(cart.get());
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        if (!replaceCartWithEmpty(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found!");
        }
        return ResponseEntity.ok("Items removed from cart successfully!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        Optional<Cart> cart = findCartByUserId(userId);
        if (cart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found!");
        }

        Optional<CartItem> itemToRemove = findItemInCart(cart.get(), productId);
        if (itemToRemove.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item found in cart with this product ID.");
        }

        removeItemFromCart(cart.get(), itemToRemove.get());
        return ResponseEntity.ok("Item removed from cart successfully!");
    }

    private Cart getOrCreateCart(Long userId) {
        return findCartByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart(userId);
            carts.add(newCart);
            return newCart;
        });
    }

    private Optional<Cart> findCartByUserId(Long userId) {
        return carts.stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst();
    }

    private void addItemToCart(Cart cart, CartItem newItem) {
        Optional<CartItem> existingItem = findItemInCart(cart, newItem.getProductId());

        if (existingItem.isPresent()) {
            updateItemQuantityAndPrice(cart, existingItem.get(), newItem.getQuantity());
        } else {
            cart.getItems().add(newItem);
            increaseTotalPrice(cart, newItem.getPrice(), newItem.getQuantity());
        }
    }

    private Optional<CartItem> findItemInCart(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private void updateItemQuantityAndPrice(Cart cart, CartItem existingItem, int additionalQty) {
        BigDecimal oldTotal = existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity()));
        cart.setTotalPrice(cart.getTotalPrice().subtract(oldTotal));

        existingItem.setQuantity(existingItem.getQuantity() + additionalQty);

        BigDecimal newTotal = existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity()));
        cart.setTotalPrice(cart.getTotalPrice().add(newTotal));
    }

    private void increaseTotalPrice(Cart cart, BigDecimal price, int quantity) {
        cart.setTotalPrice(cart.getTotalPrice().add(price.multiply(BigDecimal.valueOf(quantity))));
    }

    private boolean replaceCartWithEmpty(Long userId) {
        Optional<Cart> cart = findCartByUserId(userId);
        if (cart.isEmpty()) return false;

        Cart emptyCart = new Cart(userId);
        carts.set(carts.indexOf(cart.get()), emptyCart);
        return true;
    }

    private void removeItemFromCart(Cart cart, CartItem item) {
        cart.getItems().remove(item);
        decreaseTotalPrice(cart, item.getPrice(), item.getQuantity());
    }

    private void decreaseTotalPrice(Cart cart, BigDecimal price, int quantity) {
        cart.setTotalPrice(cart.getTotalPrice().subtract(price.multiply(BigDecimal.valueOf(quantity))));
    }
}
