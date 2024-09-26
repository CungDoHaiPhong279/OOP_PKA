package com.example.quanlybanhang;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class OrderController {

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> orderIdColumn;

    @FXML
    private TableColumn<Order, String> customerNameColumn;

    @FXML
    private TableColumn<Order, String> productsColumn;

    @FXML
    private Button addOrderButton;

    @FXML
    private Button editOrderButton;

    @FXML
    private Button deleteOrderButton;

    @FXML
    private TextField orderIdInput;

    @FXML
    private TextField customerNameInput;

    @FXML
    private TextField productsInput;

    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Thiết lập các cột cho TableView
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        productsColumn.setCellValueFactory(new PropertyValueFactory<>("products"));

        // Thiết lập danh sách dữ liệu cho bảng
        orderTable.setItems(orderList);
    }

    // Hàm xử lý khi nhấn nút "Thêm đơn hàng"
    @FXML
    public void addOrder() {
        String orderId = orderIdInput.getText();
        String customerName = customerNameInput.getText();
        String products = productsInput.getText();

        if (!orderId.isEmpty() && !customerName.isEmpty() && !products.isEmpty()) {
            // Chuyển chuỗi sản phẩm thành danh sách
            String[] productArray = products.split(",");
            List<Product> productList = new ArrayList<>();
            for (String productName : productArray) {
                productList.add(new Product("P001", productName.trim()));  // Sử dụng id mặc định cho Product
            }

            // Tạo một đơn hàng mới và thêm vào danh sách
            Order newOrder = new Order(orderId, customerName, productList);
            orderList.add(newOrder);

            // Xóa các trường nhập liệu
            clearInputs();
        }
    }

    // Hàm xử lý khi nhấn nút "Sửa đơn hàng"
    @FXML
    public void editOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            String updatedCustomerName = customerNameInput.getText();
            String updatedProducts = productsInput.getText();

            if (!updatedCustomerName.isEmpty() && !updatedProducts.isEmpty()) {
                // Cập nhật danh sách sản phẩm
                String[] productArray = updatedProducts.split(",");
                List<Product> updatedProductList = new ArrayList<>();
                for (String productName : productArray) {
                    updatedProductList.add(new Product("P001", productName.trim()));
                }

                // Cập nhật thông tin của đơn hàng
                selectedOrder.setCustomerName(updatedCustomerName);
                selectedOrder.setProducts(updatedProductList);

                // Làm mới bảng sau khi chỉnh sửa
                orderTable.refresh();

                // Xóa các trường nhập liệu
                clearInputs();
            }
        }
    }

    // Hàm xử lý khi nhấn nút "Xóa đơn hàng"
    @FXML
    public void deleteOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orderList.remove(selectedOrder);
        }
    }

    // Hàm xóa dữ liệu trong các trường nhập liệu sau khi thêm/sửa
    private void clearInputs() {
        orderIdInput.clear();
        customerNameInput.clear();
        productsInput.clear();
    }
}
