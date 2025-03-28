package com.example.demo.controller;


import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/list")
    public String getUserOrders(@RequestParam("userId") int userId, Model model) {
        // Lấy người dùng theo userId
        User user = userService.getUserById(userId);


        List<Orders> orders = orderRepository.findByUser_UserId(userId);


        model.addAttribute("user", user);
        model.addAttribute("orders", orders);


        return "orderList";
    }
}
