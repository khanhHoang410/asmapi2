package com.example.asm_and103_ph45484.Cart.Model;

public class BillModel {
    private String _id;
    private String userId;
    private String productName;
    private double productPrice;
    private String productImage;
    private String productCate;
    private String productDes;
    private double productWeight;

    public BillModel(String _id, String userId, String productName, double productPrice, String productImage, String productCate, String productDes, double productWeight) {
        this._id = _id;
        this.userId = userId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productCate = productCate;
        this.productDes = productDes;
        this.productWeight = productWeight;
    }

    public BillModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductCate() {
        return productCate;
    }

    public void setProductCate(String productCate) {
        this.productCate = productCate;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }
}
