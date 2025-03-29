package com.example.demo.service;


import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;


    @Transactional
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public void deleteOrderItem(int orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
    public List<OrderItem> getOrderItems() {
        return orderItemRepository.findAll();
    }
}
