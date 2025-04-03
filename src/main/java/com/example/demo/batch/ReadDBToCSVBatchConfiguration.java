package com.example.demo.batch;


import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class ReadDBToCSVBatchConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<Orders> dbReader() {
        JdbcCursorItemReader<Orders> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT o.orderId, o.status, o.orderDate, o.totalAmount, u.userName " +
                "FROM [DB2].[dbo].[Orders] o " +
                "JOIN [DB2].[dbo].[User] u ON o.userId = u.userId " +
                "WHERE o.orderDate > DATEADD(HOUR, -1, GETDATE());");
        reader.setRowMapper(new BeanPropertyRowMapper<>(Orders.class));
        return reader;
    }



    @Bean
    public FlatFileItemWriter<Orders> dbWriter() {
        FlatFileItemWriter<Orders> writer = new FlatFileItemWriter<>();
        writer.setName("orderItemWriter");
        writer.setResource(new FileSystemResource("exported_orders.csv"));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new CustomLineAggregator());
        return writer;
    }

    @Bean
    public Step step2(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .<Orders, Orders>chunk(10, transactionManager)
                .reader(dbReader())
                .writer(dbWriter())
                .build();
    }

    @Bean
    public Job exportDatabaseToCsvJob(JobRepository jobRepository, Step step2) {
        return new JobBuilder("exportDatabaseToCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step2)
                .build();
    }
}