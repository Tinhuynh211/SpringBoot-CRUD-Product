package com.example.demo.entity;


import java.math.BigDecimal;

public class Cart {
    private Product product;
    private int quantity;

    public Cart(int quantity, Product product) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return this.quantity * this.product.getProductPrice().toBigInteger().intValueExact();
    }
}