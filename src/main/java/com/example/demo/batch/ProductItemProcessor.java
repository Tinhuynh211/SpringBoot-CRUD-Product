package com.example.demo.batch;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    private static final Logger log = LoggerFactory.getLogger(ProductItemProcessor.class);


    @Override
    public Product process(Product product) throws Exception {

        int categoryId = product.getCategory().getCategoryId();
        Category category = new Category();
        category.setCategoryId(categoryId);

        final String productName = product.getProductName() != null ? product.getProductName().toUpperCase() : null;
        final BigDecimal productPrice = product.getProductPrice() != null ? product.getProductPrice() : BigDecimal.ZERO;
        final boolean deleteFlg = product.isDeleteFlg();

        final Product transformedProduct = new Product(productName, productPrice, category, deleteFlg);

        if (product.getProductName() == null || product.getProductPrice() == null) {
            throw new IllegalArgumentException("Missing product data");
        }
        log.info("Converting ({}) into ({})", product, transformedProduct);
        return transformedProduct;
    }
}
