package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static List<Cart> carts = new ArrayList<>();

    @PostMapping("/add")
    public String addToCart(@RequestParam Long userId, @RequestBody CartItem item) {
        Cart cart = carts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    carts.add(newCart);
                    return newCart;
                });

        cart.getItems().add(item);
        cart.setTotalPrice(cart.getTotalPrice().add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
        return "Product added to cart successfully!";
    }

    @GetMapping("/view/{userId}")
    public Cart viewCart(@PathVariable Long userId) {
        return carts.stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/clear/{userId}")
    public String removeFromCart(@PathVariable Long userId) {
        Cart cart = carts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        if (cart != null) {
            carts.set(carts.indexOf(cart),new Cart(userId));
            return "Items removed from cart successfully!";
        }
        return "Cart not found!";
    }
}
