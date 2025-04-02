package com.example.demo.repository;

import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    OrderItem findByOrdersAndProduct(Orders order, Product product);

    List<OrderItem> findByOrdersUserUserId(int userId);

    List<OrderItem> findByOrders_OrderIdAndOrders_User_UserId(int orderId, int userId);

    List<OrderItem> findByOrdersOrderId(int orderId);

    List<OrderItem> findByOrdersOrderIdAndOrdersUserUserId(int orderId, int userId);


}
