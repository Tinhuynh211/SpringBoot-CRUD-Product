package com.example.demo.batch;


import com.example.demo.entity.Orders;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OrderItemProcessor implements ItemProcessor<Orders, Orders> {

    private static final Logger log = LoggerFactory.getLogger(OrderItemProcessor.class);

   @Autowired
   private UserRepository userRepository;

    @Override
    public Orders process(Orders order) throws Exception {
        User user = userRepository.findByUserId(order.getUser().getUserId());
        order.setUser(user);
        log.info("Processing order: {} for user: {}", order.getOrderId(), user.getUserName());
        return order;
    }
}
