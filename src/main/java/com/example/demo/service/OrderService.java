package com.example.demo.service;

import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;


    @Transactional
    public void saveOrderWithItems(Orders order, List<OrderItem> orderItems) {
        Orders savedOrder = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
    }



    @Transactional
    public void confirmPayment(int orderId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(true);
            orderRepository.save(order);
        }
    }

    @Transactional
    public void cancelOrder(int orderId) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            List<OrderItem> items = orderItemRepository.findByOrdersOrderId(orderId);
            orderItemRepository.deleteAll(items);
            orderRepository.delete(order);
        }
    }
}