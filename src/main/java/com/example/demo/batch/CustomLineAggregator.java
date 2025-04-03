package com.example.demo.batch;  // Đảm bảo package này khớp với package của bạn

import org.springframework.batch.item.file.transform.LineAggregator;
import com.example.demo.entity.Orders;

public class CustomLineAggregator implements LineAggregator<Orders> {

    @Override
    public String aggregate(Orders item) {

        String userName = item.getUser() != null ? item.getUser().getUserName() : "Tin Huynh";
        String orderId = String.valueOf(item.getOrderId());
        String status = String.valueOf(item.getStatusMessage());
        String orderDate = item.getOrderDate() != null ? item.getOrderDate().toString() : "";
        String totalAmount = item.getTotalAmount() != null ? item.getTotalAmount().toString() : "0.00";


        return orderId + "," + status + "," + orderDate + "," + userName + "," + totalAmount;
    }
}
