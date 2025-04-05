package com.example.demo.batch;

import com.example.demo.entity.Product;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@ComponentScan(basePackages = "com.example.demo")
@EntityScan(basePackages = "com.example.demo.entity")
public class ReadCSVtoDBBatchConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public FlatFileItemReader<Product> reader() {
        FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("product.csv"));
        reader.setLineMapper(new DefaultLineMapper<Product>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("productName", "productPrice", "categoryId", "deleteFlg");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
                setTargetType(Product.class);
            }});
        }});
        return reader;
    }




    @Bean
    public ProductItemProcessor processor() {
        return new ProductItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Product> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
                .sql("INSERT INTO product (productName, productPrice, categoryId, deleteFlg) " +
                        "VALUES (:productName, :productPrice, :categoryId, :deleteFlg)")
                .beanMapped()
                .build();
    }


    @Bean
    public Job importProductJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("ImportProductJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<Product> reader, ProductItemProcessor processor, JdbcBatchItemWriter<Product> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Product, Product>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(10)
                .listener(new ItemReadListener<Product>() {
                    @Override
                    public void onReadError(Exception ex) {
                        System.out.println("Error reading item: " + ex.getMessage());
                    }
                    @Override
                    public void beforeRead() {}
                    @Override
                    public void afterRead(Product item) {}
                })
                .build();
    }



}
