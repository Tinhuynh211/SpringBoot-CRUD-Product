package com.example.demo.batch;

import com.example.demo.entity.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderJobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(OrderJobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public OrderJobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Order job completed. Verifying results...");

            String sql = "SELECT o.orderId, o.status, o.orderDate, o.totalAmount, u.userName " +
                    "FROM Orders o " +
                    "JOIN [User] u ON o.userId = u.userId";

            List<Orders> ordersList = jdbcTemplate.query(sql, new RowMapper<Orders>() {
                @Override
                public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Orders order = new Orders();
                    order.setOrderId(rs.getInt("orderId"));
                    order.setStatus(rs.getBoolean("status"));
                    order.setOrderDate(rs.getTimestamp("orderDate").toLocalDateTime());
                    order.setTotalAmount(rs.getBigDecimal("totalAmount"));

                    log.info("Order: ID = {}, Status = {}, Date = {}, Total = {}, User = {}",
                            order.getOrderId(), order.getStatusMessage(), order.getOrderDate(),
                            order.getTotalAmount(), rs.getString("userName"));

                    return order;
                }
            });

            log.info("Found {} orders in the database.", ordersList.size());
        }
    }
}
