package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String productName;
    private BigDecimal productPrice;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    private boolean deleteFlg;

    public Product() {}

    public Product(String productName, BigDecimal productPrice, Category category, boolean deleteFlg) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.category = category;
        this.deleteFlg = deleteFlg;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", category=" + category +
                ", deleteFlg=" + deleteFlg +
                '}';
    }
}

