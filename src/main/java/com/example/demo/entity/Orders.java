package com.example.demo.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private boolean deleteFlg;
    private boolean status;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
    public Orders() {

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getStatusMessage() {
        return status ? "Success" : "Pending";
    }


    public Orders(int orderId, User user, LocalDateTime orderDate, BigDecimal totalAmount, boolean deleteFlg, boolean status) {
        this.orderId = orderId;
        this.user = user;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.deleteFlg = deleteFlg;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    @Override
    public String toString() {
        return "Order{" +
                "OrderId=" + orderId +
                ", user=" + user +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", deleteFlg=" + deleteFlg +
                '}';
    }
}
