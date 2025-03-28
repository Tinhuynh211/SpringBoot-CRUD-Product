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
import java.util.List;

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

    // Hiển thị trang thanh toán với thông tin đơn hàng
    @GetMapping("/payment/{orderId}")
    public String showPaymentPage(@PathVariable("orderId") int orderId, @RequestParam("userId") int userId, Model model) {
        // Lấy thông tin người dùng và đơn hàng
        User user = userService.getUserById(userId);
        Orders order = orderRepository.findById(orderId).orElse(null);

        if (order == null || order.getUser().getUserId() != userId) {
            model.addAttribute("message", "Đơn hàng không hợp lệ!");
            return "errorPage";  // Trang lỗi nếu đơn hàng không hợp lệ
        }

        // Lấy danh sách OrderItem của đơn hàng
        List<OrderItem> orderItems = orderItemRepository.findByOrdersOrderId(orderId);

        // Tính tổng số tiền từ các OrderItem
        BigDecimal totalAmount = orderItems.stream()
                .map(orderItem -> orderItem.getPrize().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Thêm thông tin vào model
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("user", user);

        return "Payment";  // Trả về trang payment
    }

    // Xử lý thanh toán khi người dùng nhấn nút thanh toán
    @PostMapping("/confirm/{orderId}")
    public String processPayment(@PathVariable("orderId") int orderId, @RequestParam("userId") int userId, Model model) {
        // Lấy thông tin người dùng
        User user = userService.getUserById(userId);

        // Lấy đơn hàng hiện tại dựa trên orderId
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getUser().getUserId() != userId) {
            model.addAttribute("message", "Đơn hàng không hợp lệ!");
            return "errorPage"; // Trả về trang lỗi nếu đơn hàng không hợp lệ
        }

        // Cập nhật trạng thái của đơn hàng thành đã thanh toán
        order.setStatus(true);
        orderRepository.save(order); // Lưu lại thay đổi vào database

        // Cập nhật trang xác nhận thanh toán thành công
        model.addAttribute("message", "Thanh toán thành công!");
        model.addAttribute("order", order); // Truyền đơn hàng cho trang xác nhận
        return "orderConfirmation"; // Trang xác nhận thanh toán
    }



}
