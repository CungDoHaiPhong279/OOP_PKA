package com.example.quanlybanhang;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String rating;

    public Product(String id, String name, double price, int quantity, String rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    public Product(String id, String name) {
        this.id = id;
        this.name = name;
        this.price = 0.0; // Giá mặc định
        this.quantity = 1; // Số lượng mặc định
    }
    // Thêm phương thức getProductName
    public String getProductName() {
        return this.name;
    }
}
