package com.example.demo.service;


import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private Map<Integer, Cart> cartItems  = new HashMap();


    public void addToCart(Product product, int quantity) {
        if (cartItems.containsKey(product.getProductId())) {
            Cart item = cartItems.get(product.getProductId());
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cartItems.put(product.getProductId(), new Cart(quantity,product));
        }

    }


    public void removeProductToCart(int id) {
        cartItems.remove(id);
    }


    public void clearCart() {
        cartItems.clear();

    }
    public double getTotalAmount() {
        return cartItems.values().stream().mapToDouble(item -> item.getTotalPrice()).sum();
    }


    public Collection<Cart> getCartItems() {
        return cartItems.values();
    }
}
