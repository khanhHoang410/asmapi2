package com.example.asm_and103_ph45484.Home.Model;

public class ProductModel {
    private String _id;
    private String name;
    private double price;
    private double weight;
    private String cate;
    private String des;
    private String image;

    public ProductModel(String _id, String name, double price, double weight, String cate, String des, String image) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.cate = cate;
        this.des = des;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
