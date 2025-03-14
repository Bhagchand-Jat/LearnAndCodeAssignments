package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static List<Cart> carts = new ArrayList<>();

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestBody CartItem item) {
        Cart cart = carts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    carts.add(newCart);
                    return newCart;
                });
                CartItem cartItem= cart.getItems().stream().filter(cartItemData->item.getProductId()==cartItemData.getProductId()).findFirst().orElse(null);
        if (cartItem == null) {
            cart.getItems().add(item);
        } else {
            item.setQuantity(cartItem.getQuantity() + item.getQuantity());
            cart.getItems().set(cart.getItems().indexOf(cartItem), item);
            cart.setTotalPrice(cart.getTotalPrice()
                    .subtract(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
        }

        cart.setTotalPrice(cart.getTotalPrice().add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
        return new ResponseEntity<>("Product added to cart successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<Cart> viewCart(@PathVariable Long userId) {
        return new ResponseEntity<>(carts.stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null), HttpStatus.OK);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long userId) {
        Cart cart = carts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        if (cart != null) {
            carts.set(carts.indexOf(cart), new Cart(userId));
            return new ResponseEntity<>("Items removed from cart successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cart not found!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        Cart cart = carts.stream()
                .filter(cartFilter -> cartFilter.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        if (cart != null) {
            CartItem cartItem = cart.getItems().stream().filter(item -> item.getProductId() == productId).findFirst()
                    .orElse(null);
            if (cartItem == null) {
                return new ResponseEntity<>("No Item Found in Cart with this Id", HttpStatus.NOT_FOUND);
            }
            cart.getItems().remove(cartItem);
            cart.setTotalPrice(cart.getTotalPrice()
                    .subtract(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity()))));
            carts.set(carts.indexOf(cart), cart);
            return new ResponseEntity<>("Items removed from cart successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cart not found!", HttpStatus.NOT_FOUND);
    }

}
