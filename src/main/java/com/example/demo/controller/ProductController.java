package com.example.demo.controller;


import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UserRepository  userRepository;

    private Map<Integer, List<Product>> userCarts = new HashMap<>();
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "productList";
    }


    @GetMapping("/user")
    public String getProductsPage(@RequestParam("userId") int userId, Model model, HttpSession session ){
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/";
        }
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("products", productService.findAll());
        return "productList";
    }

















}








