package com.example.demo.controller;

import java.util.Optional;

import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    @PostMapping("/add")
    public String addToCart(@RequestParam("userId") int userId, @RequestParam("productId") int productId, Model model) {
        User user = userService.getUserById(userId);

        Optional<Product> productOtp = productService.findById(productId);
        if(productOtp.isPresent()) {
            Product product = productOtp.get();
            if (product != null) {
                cartService.addToCart(product, 1);
            }
        }

        return "redirect:/products/user?userId=" + userId;

    }

    @GetMapping("/check-cart")
    public String index(@RequestParam("userId") int userId, Model model) {
        User logginUser = userService.getUserById(userId);
        if(logginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", logginUser);
        model.addAttribute("order", new Orders());
        model.addAttribute("carts", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotalAmount());

        return "Cart";
    }


    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("userId") int userId, @RequestParam("productId") int productId, Model model) {
        User user = userService.getUserById(userId);
        cartService.removeProductToCart(productId);


        return "redirect:/cart/check-cart?userId=" + userId;
    }

    @PostMapping("/clear")
    public String clearCart(@RequestParam("userId") int userId) {
        User user = userService.getUserById(userId);
        cartService.clearCart();
        return "redirect:/cart/check-cart?userId=" + userId;
    }
}
