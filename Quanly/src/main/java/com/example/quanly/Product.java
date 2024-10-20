package com.example.quanly;

import javafx.beans.property.*;

public class Product {

    // Các thuộc tính của sản phẩm
    private IntegerProperty productId;
    private StringProperty productName;
    private StringProperty description;
    private DoubleProperty price;
    private IntegerProperty stockQuantity;
    private IntegerProperty supplierId;
    private StringProperty createdAt;

    // Constructor đầy đủ với tất cả các thuộc tính
    public Product(int productId, String productName, String description, double price, int stockQuantity, int supplierId, String createdAt) {
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity);
        this.supplierId = new SimpleIntegerProperty(supplierId);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    // Constructor ngắn với productId, productName, và price
    public Product(int productId, String productName, double price, int stockQuantity) {
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleDoubleProperty(price);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity); // Khởi tạo giá trị tồn kho mặc định là 0
    }

    // Constructor mặc định (không tham số)
    public Product() {
        this(0, "", "", 0.0, 0, 0, "");
    }

    // Getters và Setters cho productId
    public String getName() {
        return getProductName(); // Gọi hàm getProductName() đã tồn tại
    }
    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    // Getters và Setters cho productName
    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    // Getters và Setters cho description
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // Getters và Setters cho price
    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    // Getters và Setters cho stockQuantity
    public int getStockQuantity() {
        return stockQuantity.get();
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity.set(stockQuantity);
    }

    public IntegerProperty stockQuantityProperty() {
        return stockQuantity;
    }

    // Getters và Setters cho supplierId
    public int getSupplierId() {
        return supplierId.get();
    }

    public void setSupplierId(int supplierId) {
        this.supplierId.set(supplierId);
    }

    public IntegerProperty supplierIdProperty() {
        return supplierId;
    }

    // Getters và Setters cho createdAt
    public String getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }

    // Override phương thức toString để hiển thị tên sản phẩm trong ComboBox
    @Override
    public String toString() {
        return this.getProductName(); // Trả về tên sản phẩm thay vì thông tin đối tượng mặc định
    }
}
