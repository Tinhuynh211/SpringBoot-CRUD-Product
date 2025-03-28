package com.example.demo.controller;


import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/item")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/order/{orderId}/details")
    public String getOrderDetails(@PathVariable("orderId") int orderId, @RequestParam("userId") int userId, Model model) {
        List<OrderItem> orderItems = orderItemRepository.findByOrders_OrderIdAndOrders_User_UserId(orderId, userId);


        model.addAttribute("orderItems", orderItems);
        return "ordersDetails";  // Trả về trang chi tiết order
    }
}
