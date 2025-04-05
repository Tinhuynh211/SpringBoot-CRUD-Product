package com.example.demo.batch;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);

    @Override
    public Product process(Product item) throws Exception {
        if (item == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        int categoryId = item.getCategory().getCategoryId();

        Category category = new Category();
        category.setCategoryId(categoryId);

        String productName = item.getProductName() != null ? item.getProductName().toUpperCase() : null;
        BigDecimal productPrice = item.getProductPrice() != null ? item.getProductPrice() : BigDecimal.ZERO;
        item.setCategory(category);  // Gán Category vào Product
        log.info("Converting Product {} to Product with Category {}", item.getProductName(), category.getCategoryId());

        return item;
    }
}