package com.example.demo.service;


import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();

    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product getProductByName(String name) {
        return productRepository.findByProductName(name);
    }

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }
}
