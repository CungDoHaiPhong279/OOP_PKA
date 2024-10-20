package com.example.quanly;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderController {
    @FXML
    private TextField orderIdSearchField;

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
    @FXML
    private TextField customerNameField; // Khai báo cho ô nhập tên khách hàng

    @FXML
    private DatePicker orderDatePicker;  // Khai báo cho DatePicker để chọn ngày

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<Product> productComboBox; // ComboBox để chọn sản phẩm
    @FXML
    private TextField quantityField; // TextField để nhập số lượng sản phẩm

    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<OrderItem> orderItemList = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    private Connection connection;

    public void initialize() {
        // Gán các cột của bảng đơn hàng
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().orderIdProperty().asObject());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        orderDateColumn.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        // Gán các cột của bảng sản phẩm trong đơn hàng
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        loadOrdersFromDatabase();
        loadProductsToComboBox(); // Tải danh sách sản phẩm vào ComboBox
        // Lắng nghe sự kiện khi người dùng chọn một đơn hàng
        orderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showOrderDetails(newValue);
            }
        });

    }
    // Hiển thị chi tiết đơn hàng khi người dùng chọn một đơn hàng
    private void showOrderDetails(Order order) {
        customerNameField.setText(order.getCustomerName());
        orderDatePicker.setValue(LocalDateTime.parse(order.getOrderDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate());
        statusComboBox.setValue(order.getStatus());

        // Tải các sản phẩm trong đơn hàng
        loadOrderItems(order.getOrderId());
    }
    // Tải danh sách đơn hàng từ cơ sở dữ liệu
    private void loadOrdersFromDatabase() {
        orderList.clear(); // Xóa danh sách hiện tại trước khi tải lại

        try {
            connection = DBConnection.getConnection();
            // Truy vấn để lấy thông tin đơn hàng từ bảng orders
            String query = "SELECT order_id, customer_name, order_date, status, total, shipping_address FROM orders";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Lấy trực tiếp customer_name từ bảng orders
                String customerName = resultSet.getString("customer_name");

                Order order = new Order(
                        resultSet.getInt("order_id"),
                        customerName, // Sử dụng customer_name từ kết quả truy vấn
                        resultSet.getString("order_date"),
                        resultSet.getString("status"),
                        resultSet.getDouble("total"),
                        resultSet.getString("shipping_address")
                );
                orderList.add(order);
            }

            // Gán danh sách đơn hàng cho bảng
            orderTable.setItems(orderList);

        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể tải danh sách đơn hàng từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }


    // Tải danh sách sản phẩm trong đơn hàng từ cơ sở dữ liệu
    private void loadOrderItems(int orderId) {
        orderItemList.clear(); // Đảm bảo xóa danh sách cũ trước khi tải mới

        try {
            System.out.println("Loading order items for order ID: " + orderId); // Kiểm tra orderId
            // Cập nhật truy vấn để lấy product_name từ bảng products
            String query = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.price, p.product_name " +
                    "FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.product_id " +
                    "WHERE oi.order_id = ?";
            System.out.println("Query: " + query); // Kiểm tra truy vấn

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Found order item with ID: " + resultSet.getInt("order_item_id")); // Kiểm tra từng dòng dữ liệu

                // Cập nhật OrderItem với product_name
                OrderItem orderItem = new OrderItem(
                        resultSet.getInt("order_item_id"),
                        resultSet.getInt("order_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"), // Lấy tên sản phẩm
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("price")
                );
                orderItemList.add(orderItem);
            }

            System.out.println("Total order items loaded: " + orderItemList.size()); // Kiểm tra số lượng item được tải về
            orderItemsTable.setItems(orderItemList); // Gán dữ liệu vào TableView
        } catch (SQLException e) {
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể tải danh sách sản phẩm từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }


    // Tải danh sách sản phẩm từ cơ sở dữ liệu và hiển thị trong ComboBox
    private void loadProductsToComboBox() {
        productList.clear();  // Xóa danh sách sản phẩm hiện tại trước khi tải lại

        try {
            connection = DBConnection.getConnection();
            String query = "SELECT product_id, product_name, price, stock_quantity FROM products"; // Thêm stock_quantity vào câu lệnh SQL
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity") // Thêm stock_quantity vào constructor
                );
                productList.add(product);
            }

            productComboBox.setItems(productList); // Cập nhật danh sách sản phẩm trong ComboBox
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi Cơ Sở Dữ Liệu", "Không thể tải danh sách sản phẩm từ cơ sở dữ liệu.");
        }
    }

    // Thêm sản phẩm vào đơn hàng
    @FXML
    private void handleAddOrderItem() {
        try {
            Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());

            // Kiểm tra nếu sản phẩm đã được chọn
            if (selectedProduct == null) {
                showErrorAlert("Lỗi", "Vui lòng chọn sản phẩm.");
                return;
            }

            // Kiểm tra số lượng nhập vào phải lớn hơn 0
            if (quantity <= 0) {
                showErrorAlert("Lỗi", "Số lượng sản phẩm phải lớn hơn 0.");
                return;
            }

            // Kiểm tra số lượng tồn kho của sản phẩm
            int stockQuantity = selectedProduct.getStockQuantity();  // Kiểu int nên không so sánh với null
            if (quantity > stockQuantity) {
                showErrorAlert("Lỗi", "Số lượng tồn kho không đủ. Số lượng còn lại: " + stockQuantity);
                return;
            }

            // Lấy đơn hàng đã được chọn
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            double price = selectedProduct.getPrice();
            int productId = selectedProduct.getProductId();

            // Trường hợp không có đơn hàng được chọn, thêm sản phẩm vào danh sách tạm thời
            if (selectedOrder == null) {
                OrderItem orderItem = new OrderItem(productId, selectedProduct.getProductName(), quantity, price);
                orderItemList.add(orderItem);
                orderItemsTable.setItems(orderItemList);  // Cập nhật hiển thị sản phẩm trong bảng
                showSuccessAlert("Thành công", "Sản phẩm đã được thêm vào đơn hàng tạm thời.");
            } else {
                // Trường hợp có đơn hàng được chọn, thêm sản phẩm vào cơ sở dữ liệu
                String addOrderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                PreparedStatement orderItemStatement = connection.prepareStatement(addOrderItemQuery);
                orderItemStatement.setInt(1, selectedOrder.getOrderId());  // Cập nhật với order ID
                orderItemStatement.setInt(2, productId);
                orderItemStatement.setInt(3, quantity);
                orderItemStatement.setDouble(4, price);
                orderItemStatement.executeUpdate();

                // Thêm vào danh sách sản phẩm tạm thời
                OrderItem orderItem = new OrderItem(productId, selectedProduct.getProductName(), quantity, price);
                orderItemList.add(orderItem);
                orderItemsTable.setItems(orderItemList);

                // Cập nhật tổng tiền đơn hàng
                double newTotal = selectedOrder.getTotal() + (price * quantity);
                updateOrderTotal(selectedOrder.getOrderId(), newTotal);
                selectedOrder.setTotal(newTotal);

                orderTable.refresh();  // Cập nhật lại bảng hiển thị

                showSuccessAlert("Thành công", "Sản phẩm đã được thêm vào đơn hàng.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Lỗi", "Vui lòng nhập số lượng hợp lệ.");
        } catch (SQLException e) {
            showErrorAlert("Lỗi Cơ Sở Dữ Liệu", "Có lỗi xảy ra trong quá trình thêm sản phẩm vào đơn hàng.");
            e.printStackTrace();
        }
    }

    // Phương thức cập nhật số lượng tồn kho trong cơ sở dữ liệu
    private void updateProductStock(int productId, int newStockQuantity) throws SQLException {
        String updateStockQuery = "UPDATE products SET stock_quantity = ? WHERE product_id = ?";
        PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery);
        updateStockStatement.setInt(1, newStockQuantity);
        updateStockStatement.setInt(2, productId);
        updateStockStatement.executeUpdate();
    }
    // Hiển thị thông báo lỗi
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleAddOrder() {
        try {
            // Lấy thông tin khách hàng và trạng thái
            String customerName = customerNameField.getText();
            String status = statusComboBox.getValue();
            double total = 0.0;

            // Kiểm tra nếu tên khách hàng rỗng
            if (customerName == null || customerName.isEmpty()) {
                showErrorAlert("Lỗi", "Vui lòng nhập tên khách hàng.");
                return;
            }

            // Kiểm tra nếu không có sản phẩm nào trong đơn hàng
            if (orderItemList.isEmpty()) {
                showErrorAlert("Lỗi", "Vui lòng thêm ít nhất một sản phẩm vào đơn hàng.");
                return;
            }

            // Tự động thiết lập ngày đặt hàng bằng ngày và giờ hiện tại
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String orderDate = currentDateTime.format(formatter);

            // Lưu đơn hàng vào cơ sở dữ liệu và lấy ID của đơn hàng
            int orderId = saveOrderToDatabase(customerName, orderDate, status, total);

            // Lưu sản phẩm trong đơn hàng vào cơ sở dữ liệu và cập nhật tổng tiền
            for (OrderItem item : orderItemList) {
                saveOrderItemToDatabase(orderId, item);
                total += item.getPrice() * item.getQuantity(); // Tính tổng tiền
            }

            // Cập nhật tổng số tiền của đơn hàng vào cơ sở dữ liệu
            updateOrderTotal(orderId, total);

            // Thêm đơn hàng mới vào danh sách hiển thị
            Order newOrder = new Order(orderId, customerName, orderDate, status, total, "Địa chỉ giao hàng");
            orderList.add(newOrder);

            // Hiển thị thông báo thành công
            // Sau khi cập nhật thành công, hiển thị thông báo
            showSuccessAlert("Thành công", "Đơn hàng đã được thêm thành công vào lúc " + orderDate);


            // Xóa danh sách sản phẩm sau khi thêm đơn hàng
            orderItemList.clear();
            orderItemsTable.setItems(orderItemList);

        } catch (SQLException e) {
            showErrorAlert("Lỗi Cơ Sở Dữ Liệu", "Có lỗi xảy ra trong quá trình thêm đơn hàng.");
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Có lỗi xảy ra trong quá trình xử lý.");
            e.printStackTrace();
        }
    }

    // Phương thức lưu đơn hàng vào cơ sở dữ liệu
    private int saveOrderToDatabase(String customerName, String orderDate, String status, double total) throws SQLException {
        // Thay đổi câu truy vấn để lưu tên khách hàng trực tiếp vào orders
        String orderQuery = "INSERT INTO orders (customer_name, order_date, status, total) VALUES (?, ?, ?, ?)";
        PreparedStatement orderStatement = connection.prepareStatement(orderQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        orderStatement.setString(1, customerName);  // Lưu trực tiếp tên khách hàng
        orderStatement.setString(2, orderDate);
        orderStatement.setString(3, status);
        orderStatement.setDouble(4, total);
        orderStatement.executeUpdate();

        // Lấy ID của đơn hàng vừa tạo
        ResultSet generatedKeys = orderStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Tạo đơn hàng thất bại, không thể lấy ID đơn hàng.");
        }
    }


    // Phương thức lưu sản phẩm trong đơn hàng vào cơ sở dữ liệu
    private void saveOrderItemToDatabase(int orderId, OrderItem item) throws SQLException {
        String orderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        PreparedStatement orderItemStatement = connection.prepareStatement(orderItemQuery);
        orderItemStatement.setInt(1, orderId);
        orderItemStatement.setInt(2, item.getProductId());
        orderItemStatement.setInt(3, item.getQuantity());
        orderItemStatement.setDouble(4, item.getPrice());
        orderItemStatement.executeUpdate();
    }

    // Phương thức cập nhật tổng số tiền của đơn hàng
    private void updateOrderTotal(int orderId, double total) throws SQLException {
        String updateTotalQuery = "UPDATE orders SET total = ? WHERE order_id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateTotalQuery);
        updateStatement.setDouble(1, total);
        updateStatement.setInt(2, orderId);
        updateStatement.executeUpdate();
    }


    // Phương thức hiển thị thông báo lỗi
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Phương thức hiển thị thông báo thành công
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Sử dụng hộp thoại thông báo thông thường
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void handleEditOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                // Lấy tên khách hàng và trạng thái từ giao diện
                String customerName = customerNameField.getText();
                String status = statusComboBox.getValue();

                // Thiết lập ngày và giờ hiện tại (định dạng đầy đủ ngày và giờ)
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String orderDate = currentDateTime.format(formatter);

                // Cập nhật câu lệnh SQL để lưu trực tiếp tên khách hàng thay vì user_id
                String updateOrderQuery = "UPDATE orders SET customer_name = ?, status = ?, order_date = ? WHERE order_id = ?";
                PreparedStatement updateOrderStatement = connection.prepareStatement(updateOrderQuery);
                updateOrderStatement.setString(1, customerName);
                updateOrderStatement.setString(2, status);
                updateOrderStatement.setString(3, orderDate);  // Sử dụng orderDate với định dạng đầy đủ ngày và giờ
                updateOrderStatement.setInt(4, selectedOrder.getOrderId());
                updateOrderStatement.executeUpdate();

                // Cập nhật lại thông tin trong đối tượng Order và bảng hiển thị
                selectedOrder.setCustomerName(customerName);  // Đổi từ setUserId sang setCustomerName
                selectedOrder.setStatus(status);
                selectedOrder.setOrderDate(orderDate);  // Cập nhật cả thời gian vào đối tượng Order

                orderTable.refresh();  // Cập nhật lại bảng hiển thị

                showSuccessAlert("Thành công", "Đơn hàng đã được cập nhật thành công.");
            } catch (SQLException e) {
                showAlert("Lỗi", "Không thể cập nhật đơn hàng vào cơ sở dữ liệu.");
                e.printStackTrace();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn đơn hàng để chỉnh sửa.");
        }
        reset();
    }


    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                // Xóa các sản phẩm trong đơn hàng trước
                String deleteOrderItemsQuery = "DELETE FROM order_items WHERE order_id = ?";
                PreparedStatement deleteOrderItemsStatement = connection.prepareStatement(deleteOrderItemsQuery);
                deleteOrderItemsStatement.setInt(1, selectedOrder.getOrderId());
                deleteOrderItemsStatement.executeUpdate();

                // Sau đó xóa đơn hàng
                String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";
                PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderQuery);
                deleteOrderStatement.setInt(1, selectedOrder.getOrderId());
                deleteOrderStatement.executeUpdate();

                // Xóa đơn hàng khỏi danh sách
                orderList.remove(selectedOrder);

                // Xóa thông tin hiển thị
                clearOrderDetails();
            } catch (SQLException e) {
                showAlert("Lỗi", "Không thể xóa đơn hàng từ cơ sở dữ liệu.");
                e.printStackTrace();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn đơn hàng để xóa.");
        }
    }
    private void clearOrderDetails() {
        customerNameField.clear();
        statusComboBox.setValue(null);
        orderDatePicker.setValue(null);
        orderItemList.clear();
    }

    @FXML
    private void handleDeleteOrderItem() {
        // Lấy sản phẩm được chọn từ bảng orderItemsTable
        OrderItem selectedOrderItem = orderItemsTable.getSelectionModel().getSelectedItem();
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem(); // Lấy đơn hàng hiện tại

        if (selectedOrderItem != null && selectedOrder != null) {
            try {
                // Xóa sản phẩm khỏi cơ sở dữ liệu
                String deleteOrderItemQuery = "DELETE FROM order_items WHERE order_item_id = ?";
                PreparedStatement statement = connection.prepareStatement(deleteOrderItemQuery);
                statement.setInt(1, selectedOrderItem.getOrderItemId());
                statement.executeUpdate();

                // Cập nhật tổng tiền đơn hàng
                double newTotal = selectedOrder.getTotal() - (selectedOrderItem.getPrice() * selectedOrderItem.getQuantity());
                updateOrderTotal(selectedOrder.getOrderId(), newTotal);
                selectedOrder.setTotal(newTotal);

                // Xóa sản phẩm khỏi danh sách hiển thị
                orderItemList.remove(selectedOrderItem);
                orderItemsTable.setItems(orderItemList);
                orderItemsTable.refresh();  // Cập nhật lại bảng sản phẩm trong đơn hàng

                showSuccessAlert("Thành công", "Sản phẩm đã được xóa khỏi đơn hàng.");
            } catch (SQLException e) {
                showErrorAlert("Lỗi Cơ Sở Dữ Liệu", "Có lỗi xảy ra trong quá trình xóa sản phẩm khỏi đơn hàng.");
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Lỗi", "Vui lòng chọn sản phẩm để xóa.");
        }
    }

    public void handleSearchOrderById() {
        String orderIdText = orderIdSearchField.getText().trim(); // Lấy và loại bỏ khoảng trắng

        // Kiểm tra nếu trường ID đơn hàng còn trống
        if (orderIdText.isEmpty()) {
            // Hiển thị thông báo yêu cầu nhập ID
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập ID đơn hàng để tìm kiếm.");
            alert.showAndWait();
            return; // Dừng thực hiện nếu ID trống
        }

        // Nếu trường ID không trống, tiếp tục thực hiện tìm kiếm
        try {
            int orderId = Integer.parseInt(orderIdText);
            for (Order order : orderList) {
                if (order.getOrderId() == orderId) {
                    orderTable.getSelectionModel().select(order);
                    return;  // Dừng sau khi tìm thấy đơn hàng
                }
            }
            // Hiển thị thông báo nếu không tìm thấy
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy đơn hàng với ID: " + orderId);
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Hiển thị thông báo nếu ID không hợp lệ
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập ID hợp lệ.");
            alert.showAndWait();
        }
    }
    private void reset() {
        // Làm mới các trường nhập liệu
        customerNameField.clear(); // Làm mới trường nhập tên khách hàng
        orderDatePicker.setValue(null); // Đặt lại ngày đặt hàng
        statusComboBox.getSelectionModel().clearSelection(); // Xóa lựa chọn trạng thái

        // Xóa lựa chọn trong bảng đơn hàng và sản phẩm
        orderTable.getSelectionModel().clearSelection(); // Xóa lựa chọn trong bảng đơn hàng
        orderItemsTable.getSelectionModel().clearSelection(); // Xóa lựa chọn trong bảng sản phẩm trong đơn hàng

        // Xóa sản phẩm trong bảng sản phẩm của đơn hàng
        orderItemList.clear(); // Xóa tất cả sản phẩm trong danh sách
        orderItemsTable.setItems(FXCollections.observableArrayList(orderItemList)); // Cập nhật bảng sản phẩm

        // Reset phần chọn sản phẩm và nhập số lượng
        productComboBox.getSelectionModel().clearSelection(); // Xóa lựa chọn sản phẩm
        quantityField.clear(); // Làm trống trường nhập số lượng

        // Nếu cần thiết, có thể làm mới các danh sách hiển thị
        orderItemsTable.setItems(FXCollections.observableArrayList(orderItemList)); // Đặt lại danh sách sản phẩm
    }
    @FXML
    private void handleGoBack() {
        try {
            // Tải giao diện product.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
            Parent productView = loader.load();

            // Tạo một cảnh mới với giao diện sản phẩm
            Stage stage = (Stage) orderTable.getScene().getWindow(); // Lấy cửa sổ hiện tại
            Scene scene = new Scene(productView);

            // Đặt cảnh mới cho cửa sổ
            stage.setScene(scene);
            stage.setTitle("Quản lý Sản phẩm"); // Tùy chỉnh tiêu đề cửa sổ
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Không thể tải giao diện sản phẩm.");
        }
    }
}
