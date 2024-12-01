package com.example.asm_and103_ph45484.Notification;

import java.util.List;

import com.example.asm_and103_ph45484.Home.Model.ProductModel;

public class HistoryModel {
    private String userId;
    private List<ProductModel> products;
    private double totalPrice;

    public HistoryModel(String userId, List<ProductModel> products, double totalPrice) {
        this.userId = userId;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public HistoryModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
