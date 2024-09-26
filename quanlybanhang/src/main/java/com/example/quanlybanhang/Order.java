package com.example.quanlybanhang;

import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private String id;
    private String customerName;
    private List<Product> products;

    // Constructor
    public Order(String id, String customerName, List<Product> products) {
        this.id = id;
        this.customerName = customerName;
        this.products = products;
    }

    // Getters và setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Trả về chuỗi sản phẩm (sử dụng để hiển thị trong bảng)
    public String getProductsAsString() {
        return products.stream()
                .map(Product::getProductName) // Lấy tên sản phẩm
                .collect(Collectors.joining(", ")); // Nối thành chuỗi, phân tách bởi dấu phẩy
    }
}
