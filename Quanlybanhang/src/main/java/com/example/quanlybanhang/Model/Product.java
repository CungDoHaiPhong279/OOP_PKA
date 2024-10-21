package com.example.quanlybanhang.Model;

import java.util.Objects;

public class Product {
    private String id;
    private String name;
    private double retailPrice;
    private double costPrice;
    private int stock;
    private int sold;
    private String expiryDate;

    public Product(String id, String name, double retailPrice, double costPrice, int stock, int sold, String expiryDate) {
        if (retailPrice < 0 || costPrice < 0 || stock < 0 || sold < 0) {
            throw new IllegalArgumentException("Prices, stock, and sold quantities must not be negative");
        }
        this.id = id;
        this.name = name;
        this.retailPrice = retailPrice;
        this.costPrice = costPrice;
        this.stock = stock;
        this.sold = sold;
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getRetailPrice() { return retailPrice; }
    public void setRetailPrice(double retailPrice) {
        if (retailPrice < 0) throw new IllegalArgumentException("Retail price cannot be negative");
        this.retailPrice = retailPrice;
    }

    public double getCostPrice() { return costPrice; }
    public void setCostPrice(double costPrice) {
        if (costPrice < 0) throw new IllegalArgumentException("Cost price cannot be negative");
        this.costPrice = costPrice;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.stock = stock;
    }

    public int getSold() { return sold; }
    public void setSold(int sold) {
        if (sold < 0) throw new IllegalArgumentException("Sold quantity cannot be negative");
        this.sold = sold;
    }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    // toString method
    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', retailPrice=%.2f, costPrice=%.2f, stock=%d, sold=%d, expiryDate='%s'}",
                id, name, retailPrice, costPrice, stock, sold, expiryDate);
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.retailPrice, retailPrice) == 0 &&
                Double.compare(product.costPrice, costPrice) == 0 &&
                stock == product.stock &&
                sold == product.sold &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(expiryDate, product.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, retailPrice, costPrice, stock, sold, expiryDate);
    }
}
