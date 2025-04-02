package com.example.demo.controller;


import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/list")
    public String getUserOrders(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        List<Orders> orders = orderRepository.findByUser_UserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "orderList";
    }

    @GetMapping("/payment/{orderId}")
    public String showPaymentPage(@PathVariable("orderId") int orderId, @RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        Orders order = orderRepository.findById(orderId).orElse(null);

        if (order == null || order.getUser().getUserId() != userId) {
            model.addAttribute("message", "Đơn hàng không hợp lệ!");
            return "errorPage";
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrdersOrderId(orderId);

        BigDecimal totalAmount = orderItems.stream()
                .map(orderItem -> orderItem.getPrize().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", user);

        return "Payment";
    }

    @PostMapping("/confirm/{orderId}")
    public String processPayment(@PathVariable("orderId") int orderId, @RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);

        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getUser().getUserId() != userId) {
            model.addAttribute("message", "Đơn hàng không hợp lệ!");
            return "errorPage";
        }

        order.setStatus(true);
        orderRepository.save(order);
        model.addAttribute("user", user);
        model.addAttribute("message", "Thanh toán thành công!");
        model.addAttribute("order", order);
        return "orderConfirmation";
    }

    @GetMapping ("/list/filtered")
    public String processFilteredPayment(@RequestParam("userId") int userId, Model model) {
        BigDecimal amount1 = new BigDecimal("10000");
        User user = userService.getUserById(userId);
        List<Orders> orders = orderRepository.findByUser_UserId(userId);
        List <Orders> ordersFiltered = orders.stream().filter(order -> order.getTotalAmount().compareTo(amount1) > 0).collect(Collectors.toList());
        System.out.println(ordersFiltered);
        model.addAttribute("user", user);
        model.addAttribute("ordersFiltered", ordersFiltered);
        return "filteredList";
    };

    @GetMapping("/list/getMonth")
    public String getOrderInMonth(@RequestParam("userId") int userId, Model model){
        BigDecimal amount = new BigDecimal("0");
        LocalDateTime now = LocalDateTime.now();
        Month currentMonth = now.getMonth();

        User user = userService.getUserById(userId);
        List<Orders> orders = orderRepository.findByUser_UserId(userId);
        List<Orders> orderListFiltered = orders.stream().filter(order -> order.getOrderDate().getMonth().equals(currentMonth)).collect(Collectors.toList());
        BigDecimal totalAmountInMonth = orderListFiltered.stream().map(Orders::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (orderListFiltered.isEmpty()) {
            totalAmountInMonth = BigDecimal.ZERO;
        }
        model.addAttribute("user", user);
        model.addAttribute("orderListFiltered", orderListFiltered);
        model.addAttribute("totalAmountInMonth",totalAmountInMonth);
        return"filteredListInMonth";

    }



}
