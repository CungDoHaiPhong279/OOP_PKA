package com.example.quanly;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.*;

public class OrderController {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, String> userIdColumn;
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML
    private TableColumn<Order, Double> totalColumn;

    @FXML
    private TableView<OrderItem> orderItemsTable;
    @FXML
    private TableColumn<OrderItem, String> productNameColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> priceColumn;

    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<OrderItem> orderItemList = FXCollections.observableArrayList();
    private Connection connection;

    public void initialize() {
        // Gán các cột của bảng đơn hàng
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        // Gán các cột của bảng sản phẩm trong đơn hàng
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        loadOrdersFromDatabase();
    }

    // Tải danh sách đơn hàng từ cơ sở dữ liệu
    private void loadOrdersFromDatabase() {
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT o.order_id, u.name AS user_name, o.order_date, o.status, o.total, o.shipping_address " +
                    "FROM orders o " +
                    "JOIN users u ON o.user_id = u.user_id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("order_date"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total"),
                        resultSet.getString("shipping_address")
                );
                orderList.add(order);
            }
            orderTable.setItems(orderList);
        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể tải danh sách đơn hàng từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    // Tải danh sách sản phẩm trong đơn hàng từ cơ sở dữ liệu
    private void loadOrderItems(int orderId) {
        try {
            orderItemList.clear();
            String query = "SELECT * FROM order_items WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrderItem orderItem = new OrderItem(
                        resultSet.getInt("order_item_id"),
                        resultSet.getInt("order_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price")
                );
                orderItemList.add(orderItem);
            }

            orderItemsTable.setItems(orderItemList);
        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể tải danh sách sản phẩm từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    // Thêm đơn hàng mới
    @FXML
    private void handleAddOrder() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Thêm Đơn Hàng Mới");

        GridPane grid = new GridPane();
        ComboBox<String> userBox = new ComboBox<>();
        DatePicker orderDatePicker = new DatePicker();
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Pending", "Shipped", "Delivered");

        TableView<Product> productTable = new TableView<>();
        ObservableList<Product> productList = loadProducts();
        productTable.setItems(productList);
        TextField quantityField = new TextField();

        grid.add(new Label("Khách hàng:"), 0, 0);
        grid.add(userBox, 1, 0);
        grid.add(new Label("Ngày đặt hàng:"), 0, 1);
        grid.add(orderDatePicker, 1, 1);
        grid.add(new Label("Trạng thái:"), 0, 2);
        grid.add(statusBox, 1, 2);
        grid.add(new Label("Sản phẩm:"), 0, 3);
        grid.add(productTable, 1, 3);
        grid.add(new Label("Số lượng:"), 0, 4);
        grid.add(quantityField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    String user = userBox.getValue();
                    String orderDate = orderDatePicker.getValue().toString();
                    String status = statusBox.getValue();
                    Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
                    int quantity = Integer.parseInt(quantityField.getText());

                    if (selectedProduct == null) {
                        showAlert("Lỗi", "Vui lòng chọn một sản phẩm.");
                        return null;
                    }

                    double price = selectedProduct.getPrice();
                    double total = price * quantity;

                    Order newOrder = saveOrderToDatabase(user, orderDate, status, selectedProduct.getProductId(), quantity, price, total);
                    if (newOrder != null) {
                        orderList.add(newOrder);
                    }
                } catch (NumberFormatException e) {
                    showAlert("Lỗi", "Vui lòng nhập đúng giá trị cho các trường.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Tải danh sách sản phẩm từ cơ sở dữ liệu
    private ObservableList<Product> loadProducts() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        try {
            String query = "SELECT product_id, product_name, price FROM products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productList.add(new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price")
                ));
            }

        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể tải danh sách sản phẩm từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
        return productList;
    }

    // Lưu đơn hàng vào cơ sở dữ liệu
    private Order saveOrderToDatabase(String user, String orderDate, String status, int productId, int quantity, double price, double total) {
        try {
            String orderQuery = "INSERT INTO orders (user_id, order_date, status, total) VALUES ((SELECT user_id FROM users WHERE name = ?), ?, ?, ?)";
            PreparedStatement orderStatement = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
            orderStatement.setString(1, user);
            orderStatement.setString(2, orderDate);
            orderStatement.setString(3, status);
            orderStatement.setDouble(4, total);
            int rowsAffected = orderStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = orderStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // Lưu các sản phẩm trong đơn hàng vào bảng order_items
                    String orderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                    PreparedStatement orderItemStatement = connection.prepareStatement(orderItemQuery);
                    orderItemStatement.setInt(1, orderId);
                    orderItemStatement.setInt(2, productId);
                    orderItemStatement.setInt(3, quantity);
                    orderItemStatement.setDouble(4, price);
                    orderItemStatement.executeUpdate();

                    return new Order(orderId, user, orderDate, status, total, "Địa chỉ giao hàng");
                }
            }
        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể lưu đơn hàng vào cơ sở dữ liệu.");
            e.printStackTrace();
        }
        return null;
    }

    // Xóa đơn hàng
    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                String query = "DELETE FROM orders WHERE order_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedOrder.getOrderId());
                statement.executeUpdate();

                orderList.remove(selectedOrder);
            } catch (SQLException e) {
                showAlert("Lỗi", "Không thể xóa đơn hàng từ cơ sở dữ liệu.");
                e.printStackTrace();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn đơn hàng để xóa.");
        }
    }

    // Chỉnh sửa đơn hàng
    @FXML
    private void handleEditOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            Dialog<Order> dialog = new Dialog<>();
            dialog.setTitle("Chỉnh Sửa Đơn Hàng");

            GridPane grid = new GridPane();
            ComboBox<String> userBox = new ComboBox<>();
            DatePicker orderDatePicker = new DatePicker();
            ComboBox<String> statusBox = new ComboBox<>();
            statusBox.getItems().addAll("Pending", "Shipped", "Delivered");

            userBox.setValue(selectedOrder.getUserId());
            orderDatePicker.setValue(java.time.LocalDate.parse(selectedOrder.getOrderDate()));
            statusBox.setValue(selectedOrder.getStatus());

            TableView<Product> productTable = new TableView<>();
            ObservableList<Product> productList = loadProducts();
            productTable.setItems(productList);
            TextField quantityField = new TextField();
            quantityField.setText(String.valueOf(selectedOrder.getTotal()));

            grid.add(new Label("Khách hàng:"), 0, 0);
            grid.add(userBox, 1, 0);
            grid.add(new Label("Ngày đặt hàng:"), 0, 1);
            grid.add(orderDatePicker, 1, 1);
            grid.add(new Label("Trạng thái:"), 0, 2);
            grid.add(statusBox, 1, 2);
            grid.add(new Label("Sản phẩm:"), 0, 3);
            grid.add(productTable, 1, 3);
            grid.add(new Label("Số lượng:"), 0, 4);
            grid.add(quantityField, 1, 4);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(button -> {
                if (button == ButtonType.OK) {
                    try {
                        String user = userBox.getValue();
                        String orderDate = orderDatePicker.getValue().toString();
                        String status = statusBox.getValue();
                        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
                        int quantity = Integer.parseInt(quantityField.getText());

                        if (selectedProduct == null) {
                            showAlert("Lỗi", "Vui lòng chọn một sản phẩm.");
                            return null;
                        }

                        double price = selectedProduct.getPrice();
                        double total = price * quantity;

                        // Cập nhật đơn hàng trong cơ sở dữ liệu
                        updateOrderInDatabase(selectedOrder.getOrderId(), user, orderDate, status, selectedProduct.getProductId(), quantity, price, total);

                        selectedOrder.setUserId(user);
                        selectedOrder.setOrderDate(orderDate);
                        selectedOrder.setStatus(status);
                        selectedOrder.setTotal(total);

                        orderTable.refresh();

                    } catch (NumberFormatException e) {
                        showAlert("Lỗi", "Vui lòng nhập đúng giá trị cho các trường.");
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } else {
            showAlert("Lỗi", "Vui lòng chọn đơn hàng để chỉnh sửa.");
        }
    }

    private void updateOrderInDatabase(int orderId, String user, String orderDate, String status, int productId, int quantity, double price, double total) {
        try {
            String orderQuery = "UPDATE orders SET user_id = (SELECT user_id FROM users WHERE name = ?), order_date = ?, status = ?, total = ? WHERE order_id = ?";
            PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
            orderStatement.setString(1, user);
            orderStatement.setString(2, orderDate);
            orderStatement.setString(3, status);
            orderStatement.setDouble(4, total);
            orderStatement.setInt(5, orderId);
            orderStatement.executeUpdate();

            String orderItemQuery = "UPDATE order_items SET product_id = ?, quantity = ?, price = ? WHERE order_id = ?";
            PreparedStatement orderItemStatement = connection.prepareStatement(orderItemQuery);
            orderItemStatement.setInt(1, productId);
            orderItemStatement.setInt(2, quantity);
            orderItemStatement.setDouble(3, price);
            orderItemStatement.setInt(4, orderId);
            orderItemStatement.executeUpdate();

        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể cập nhật đơn hàng vào cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Xử lý thêm sản phẩm vào đơn hàng
    @FXML
    private void handleAddOrderItem() {
        System.out.println("Thêm sản phẩm vào đơn hàng");
    }
}
