package com.example.quanly;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private IntegerProperty orderId;
    private StringProperty userId;
    private StringProperty orderDate;
    private StringProperty status;
    private DoubleProperty total;
    private StringProperty shippingAddress;
    private List<OrderItem> orderItems;  // Chứa nhiều sản phẩm

    // Constructor đầy đủ
    public Order(int orderId, String userId, String orderDate, String status, double total, String shippingAddress) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.userId = new SimpleStringProperty(userId);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.status = new SimpleStringProperty(status);
        this.total = new SimpleDoubleProperty(total);
        this.shippingAddress = new SimpleStringProperty(shippingAddress);
        this.orderItems = new ArrayList<>();
    }

    // Constructor không có orderItems để thêm dần các sản phẩm vào sau
    public Order(int orderId, String userId, String orderDate, String status, double total, String shippingAddress, List<OrderItem> orderItems) {
        this(orderId, userId, orderDate, status, total, shippingAddress);
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
    }

    // Getters và Setters
    public int getOrderId() {
        return orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public StringProperty orderDateProperty() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate.set(orderDate);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public String getShippingAddress() {
        return shippingAddress.get();
    }

    public StringProperty shippingAddressProperty() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress.set(shippingAddress);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Thêm một sản phẩm vào đơn hàng
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        this.total.set(this.total.get() + orderItem.getPrice() * orderItem.getQuantity());
    }

    // Xóa một sản phẩm khỏi đơn hàng
    public void removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        this.total.set(this.total.get() - orderItem.getPrice() * orderItem.getQuantity());
    }

}
