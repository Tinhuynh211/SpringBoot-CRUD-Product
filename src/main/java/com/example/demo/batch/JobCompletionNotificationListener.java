package com.example.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;
import com.example.demo.entity.Product;


@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job completed");

            try {
                jdbcTemplate
                        .query("SELECT productName, productPrize, categoryId, deleteFlg FROM product", new DataClassRowMapper<>(Product.class))
                        .forEach(product -> {

                            if (product != null) {

                                log.info("Found product: Name = {}, Price = {}, Category ID = {}, Delete Flag = {}",
                                        product.getProductName(),
                                        product.getProductPrice(),
                                        product.getCategory().getCategoryId(),
                                        product.isDeleteFlg());
                            } else {
                                log.warn("Found a null product in the database.");
                            }
                        });
            } catch (Exception e) {
                log.error("Error occurred while querying the database: {}", e.getMessage());
            }
        }
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            jobExecution.setStatus(BatchStatus.STOPPED);
            log.info("Job stopped due to error: {}", jobExecution.getExitStatus());
        }
    }
}
