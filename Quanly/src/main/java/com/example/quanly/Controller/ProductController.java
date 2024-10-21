package com.example.quanly.Controller;

import com.example.quanly.Model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ProductController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> columnProductId;
    @FXML
    private TableColumn<Product, String> columnProductName;
    @FXML
    private TableColumn<Product, String> columnDescription;
    @FXML
    private TableColumn<Product, Double> columnPrice;
    @FXML
    private TableColumn<Product, Integer> columnStockQuantity;
    @FXML
    private TableColumn<Product, Integer> columnSupplierId;
    @FXML
    private TableColumn<Product, String> columnCreatedAt;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private Connection connection;

    // Khởi tạo kết nối cơ sở dữ liệu và hiển thị sản phẩm
    public void initialize() {
        columnProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        columnProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        columnDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        columnPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        columnStockQuantity.setCellValueFactory(cellData -> cellData.getValue().stockQuantityProperty().asObject());
        columnSupplierId.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty().asObject());
        columnCreatedAt.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());

        loadProductsFromDatabase();
    }

    // Tải dữ liệu sản phẩm từ cơ sở dữ liệu MySQL
    private void loadProductsFromDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "SELECT * FROM products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("created_at")
                );
                productList.add(product);
            }
            productTable.setItems(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thêm sản phẩm mới
    @FXML
    private void handleAddProduct() {
        // Tạo dialog để nhập thông tin sản phẩm
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Thêm Sản Phẩm Mới");
        dialog.setHeaderText("Nhập thông tin sản phẩm:");

        // Sử dụng GridPane để bố trí các trường nhập liệu
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Tên sản phẩm");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Mô tả");

        TextField priceField = new TextField();
        priceField.setPromptText("Giá sản phẩm");

        TextField stockField = new TextField();
        stockField.setPromptText("Số lượng tồn kho");

        TextField supplierField = new TextField();
        supplierField.setPromptText("Mã nhà cung cấp");

        grid.add(new Label("Tên sản phẩm:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Mô tả:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Giá sản phẩm:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Số lượng tồn kho:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Mã nhà cung cấp:"), 0, 4);
        grid.add(supplierField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                // Kiểm tra nếu người dùng đã nhập đầy đủ thông tin
                if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                        priceField.getText().isEmpty() || stockField.getText().isEmpty() ||
                        supplierField.getText().isEmpty()) {
                    // Hiển thị thông báo lỗi nếu còn trường nào trống
                    showAlert("Lỗi", "Bạn phải nhập đầy đủ thông tin cho tất cả các trường.");
                    return null;
                }

                try {
                    // Chuyển đổi các giá trị và thêm sản phẩm mới
                    String productName = nameField.getText();
                    String description = descriptionField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int stockQuantity = Integer.parseInt(stockField.getText());
                    int supplierId = Integer.parseInt(supplierField.getText());
                    // Kiểm tra sự tồn tại của nhà cung cấp
                    if (!isSupplierIdValid(supplierId)) {
                        showAlert("Lỗi", "Nhà cung cấp không tồn tại.");
                        return null;
                    }


                    // Lưu sản phẩm vào cơ sở dữ liệu và trả về sản phẩm đã lưu với product_id và created_at
                    Product newProduct = saveProductToDatabase(new Product(0, productName, description, price, stockQuantity, supplierId, ""));

                    if (newProduct != null) {
                        productList.add(newProduct); // Thêm sản phẩm vào danh sách hiển thị
                    }
                    return newProduct;
                } catch (NumberFormatException e) {
                    showAlert("Lỗi định dạng", "Giá sản phẩm, số lượng tồn kho và mã nhà cung cấp phải là số.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(product -> productTable.refresh());
    }


    // Thêm sản phẩm vào cơ sở dữ liệu và trả về sản phẩm với id và ngày tạo
    private Product saveProductToDatabase(Product product) {
        Product savedProduct = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "INSERT INTO products (product_name, description, price, stock_quantity, supplier_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setInt(5, product.getSupplierId());

            // Thực thi truy vấn và lấy product_id tự động tăng
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int productId = generatedKeys.getInt(1);

                    // Lấy giá trị `created_at` ngay sau khi thêm
                    String createdAtQuery = "SELECT created_at FROM products WHERE product_id = ?";
                    PreparedStatement createdAtStatement = connection.prepareStatement(createdAtQuery);
                    createdAtStatement.setInt(1, productId);
                    ResultSet createdAtResult = createdAtStatement.executeQuery();
                    if (createdAtResult.next()) {
                        String createdAt = createdAtResult.getString("created_at");

                        // Tạo đối tượng sản phẩm với thông tin đầy đủ
                        savedProduct = new Product(productId, product.getProductName(), product.getDescription(),
                                product.getPrice(), product.getStockQuantity(), product.getSupplierId(), createdAt);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedProduct;
    }

    // Sửa sản phẩm đã chọn
    // Sửa sản phẩm đã chọn
    @FXML
    private void handleEditProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Dialog<Product> dialog = new Dialog<>();
            dialog.setTitle("Sửa Sản Phẩm");
            dialog.setHeaderText("Chỉnh sửa thông tin sản phẩm:");

            // Sử dụng GridPane để chỉnh sửa dữ liệu
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField(selectedProduct.getProductName());
            TextField descriptionField = new TextField(selectedProduct.getDescription());
            TextField priceField = new TextField(Double.toString(selectedProduct.getPrice()));
            TextField stockField = new TextField(Integer.toString(selectedProduct.getStockQuantity()));
            TextField supplierField = new TextField(Integer.toString(selectedProduct.getSupplierId()));

            grid.add(new Label("Tên sản phẩm:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Mô tả:"), 0, 1);
            grid.add(descriptionField, 1, 1);
            grid.add(new Label("Giá sản phẩm:"), 0, 2);
            grid.add(priceField, 1, 2);
            grid.add(new Label("Số lượng tồn kho:"), 0, 3);
            grid.add(stockField, 1, 3);
            grid.add(new Label("Mã nhà cung cấp:"), 0, 4);
            grid.add(supplierField, 1, 4);

            dialog.getDialogPane().setContent(grid);
            ButtonType editButtonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    try {
                        int supplierId = Integer.parseInt(supplierField.getText());

                        // Kiểm tra nếu nhà cung cấp tồn tại
                        if (!isSupplierIdValid(supplierId)) {
                            showAlert("Lỗi", "Nhà cung cấp không tồn tại.");
                            return null; // Dừng quá trình chỉnh sửa nếu nhà cung cấp không tồn tại
                        }

                        // Nếu nhà cung cấp hợp lệ, cập nhật các giá trị sản phẩm
                        selectedProduct.setProductName(nameField.getText());
                        selectedProduct.setDescription(descriptionField.getText());
                        selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                        selectedProduct.setStockQuantity(Integer.parseInt(stockField.getText()));
                        selectedProduct.setSupplierId(supplierId);

                        updateProductInDatabase(selectedProduct);
                        return selectedProduct;
                    } catch (NumberFormatException e) {
                        showAlert("Lỗi định dạng", "Giá sản phẩm, số lượng tồn kho và mã nhà cung cấp phải là số.");
                    }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(product -> productTable.refresh());
        } else {
            showAlert("Không có sản phẩm nào được chọn", "Hãy chọn một sản phẩm để chỉnh sửa.");
        }
    }

    private void updateProductInDatabase(Product product) {
        try {
            String query = "UPDATE products SET product_name = ?, description = ?, price = ?, stock_quantity = ?, supplier_id = ? WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setInt(5, product.getSupplierId());
            statement.setInt(6, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa sản phẩm đã chọn
    @FXML
    private void handleDeleteProduct() {
        // Lấy sản phẩm đã chọn
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                // Kết nối tới cơ sở dữ liệu
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlybanhang", "root", "123456");

                // Kiểm tra xem sản phẩm có trong bất kỳ đơn hàng nào không
                String checkOrderItemQuery = "SELECT COUNT(*) FROM order_items WHERE product_id = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkOrderItemQuery);
                checkStatement.setInt(1, selectedProduct.getProductId());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    // Nếu sản phẩm đang có trong đơn hàng, hiển thị thông báo lỗi
                    showAlert("Lỗi", "Sản phẩm đang có trong đơn hàng và không thể xóa.");
                    return;
                }

                // Xóa sản phẩm đã chọn
                String deleteQuery = "DELETE FROM products WHERE product_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, selectedProduct.getProductId());
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    productList.remove(selectedProduct); // Xóa sản phẩm khỏi danh sách hiển thị

                    // Cập nhật lại product_id cho tất cả các sản phẩm để đảm bảo ID liên tục (nếu cần)
                    reorderProductIds(connection);

                    // Reset AUTO_INCREMENT dựa trên giá trị lớn nhất mới
                    int maxProductId = getMaxProductId(connection);
                    resetAutoIncrement(connection, maxProductId + 1);
                }

                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Lỗi", "Vui lòng chọn sản phẩm để xóa.");
        }
    }

    // Phương thức cập nhật lại product_id cho tất cả các sản phẩm
    private void reorderProductIds(Connection connection) throws SQLException {
        String selectQuery = "SELECT product_id FROM products ORDER BY product_id ASC";
        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = selectStatement.executeQuery();

        int newId = 1;
        while (resultSet.next()) {
            int currentId = resultSet.getInt("product_id");

            // Cập nhật lại product_id nếu không khớp với thứ tự mới
            if (currentId != newId) {
                String updateQuery = "UPDATE products SET product_id = ? WHERE product_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, newId);
                updateStatement.setInt(2, currentId);
                updateStatement.executeUpdate();
            }
            newId++;
        }
    }

    // Phương thức để lấy giá trị product_id lớn nhất trong bảng products
    private int getMaxProductId(Connection connection) throws SQLException {
        String maxIdQuery = "SELECT MAX(product_id) FROM products";
        PreparedStatement maxIdStatement = connection.prepareStatement(maxIdQuery);
        ResultSet maxIdResult = maxIdStatement.executeQuery();
        if (maxIdResult.next()) {
            return maxIdResult.getInt(1); // Trả về giá trị lớn nhất của product_id
        }
        return 0; // Nếu không có sản phẩm nào, trả về 0
    }

    // Phương thức để reset giá trị AUTO_INCREMENT cho bảng products
    private void resetAutoIncrement(Connection connection, int newAutoIncrement) throws SQLException {
        String resetQuery = "ALTER TABLE products AUTO_INCREMENT = ?";
        PreparedStatement resetStatement = connection.prepareStatement(resetQuery);
        resetStatement.setInt(1, newAutoIncrement);
        resetStatement.executeUpdate();
    }
    // Phương thức làm mới bảng sản phẩm
    private void refreshTable() {
        productList.clear(); // Xóa tất cả sản phẩm trong danh sách hiện tại
        loadProductsFromDatabase(); // Tải lại dữ liệu sản phẩm từ cơ sở dữ liệu
    }

    @FXML
    private void refreshProductTable() {
        productList.clear(); // Xóa toàn bộ sản phẩm hiện có trong danh sách
        loadProductsFromDatabase(); // Tải lại dữ liệu từ cơ sở dữ liệu và hiển thị
        productTable.refresh(); // Làm mới giao diện bảng
    }

    private void deleteProductFromDatabase(Product product) {
        try {
            String query = "DELETE FROM products WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            // Tải FXML cho trang đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanly/login.fxml"));
            Parent root = loader.load();

            // Lấy Stage hiện tại
            Stage stage = (Stage) productTable.getScene().getWindow(); // Giả sử productTable là một thành phần trong Scene hiện tại

            // Thiết lập Scene mới và hiển thị
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleGoToOrders() {
        try {
            // Load FXML cho trang Order
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanly/order.fxml")); // Đảm bảo đường dẫn đúng
            Parent orderRoot = loader.load();

            // Lấy Stage hiện tại từ một thành phần trong scene
            Stage stage = (Stage) productTable.getScene().getWindow();  // 'productTable' là một thành phần trong giao diện hiện tại của bạn
            Scene orderScene = new Scene(orderRoot);

            // Chuyển sang Scene của trang Đơn hàng (Order)
            stage.setScene(orderScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load order scene: " + e.getMessage());
        }
    }
    private boolean isSupplierIdValid(int supplierId) {
        boolean isValid = false;
        try {
            // Kết nối tới cơ sở dữ liệu
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");

            // Câu lệnh SQL để kiểm tra sự tồn tại của supplier_id
            String query = "SELECT COUNT(*) FROM suppliers WHERE supplier_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, supplierId);
            ResultSet rs = statement.executeQuery();

            // Kiểm tra nếu có kết quả trả về
            if (rs.next()) {
                isValid = rs.getInt(1) > 0; // Nếu COUNT > 0, nhà cung cấp tồn tại
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();

        // Nếu ô tìm kiếm trống, hiển thị lại tất cả sản phẩm
        if (keyword.isEmpty()) {
            productTable.setItems(productList);
        } else {
            // Tìm các sản phẩm có tên chứa từ khóa
            ObservableList<Product> filteredList = FXCollections.observableArrayList();

            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(keyword)) {
                    filteredList.add(product);
                }
            }

            // Cập nhật TableView với danh sách sản phẩm đã lọc
            productTable.setItems(filteredList);
        }
    }
}



