package com.example.quanlybanhang;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class ProductController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, String> ratingColumn;

    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productQuantityField;
    @FXML
    private TextField productRatingField;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Khởi tạo các cột trong bảng
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Thiết lập dữ liệu cho bảng
        productTable.setItems(productList);

        // Thêm sản phẩm mẫu để thử nghiệm
        productList.add(new Product("001", "Laptop", 1500.0, 10, "5 Stars"));
        productList.add(new Product("002", "Smartphone", 800.0, 25, "4.5 Stars"));

        // Sự kiện khi chọn sản phẩm trong bảng
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillProductDetails(newSelection);
            }
        });
    }

    // Điền chi tiết sản phẩm vào các trường khi chọn sản phẩm
    private void fillProductDetails(Product product) {
        productIdField.setText(product.getId());
        productNameField.setText(product.getName());
        productPriceField.setText(String.valueOf(product.getPrice()));
        productQuantityField.setText(String.valueOf(product.getQuantity()));
        productRatingField.setText(product.getRating());
    }

    // Hàm thêm sản phẩm mới
    @FXML
    private void addProduct() {
        Product product = new Product(
                productIdField.getText(),
                productNameField.getText(),
                Double.parseDouble(productPriceField.getText()),
                Integer.parseInt(productQuantityField.getText()),
                productRatingField.getText()
        );
        productList.add(product);
        clearInputFields();
    }

    // Hàm sửa sản phẩm
    @FXML
    private void editProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            selectedProduct.setId(productIdField.getText());
            selectedProduct.setName(productNameField.getText());
            selectedProduct.setPrice(Double.parseDouble(productPriceField.getText()));
            selectedProduct.setQuantity(Integer.parseInt(productQuantityField.getText()));
            selectedProduct.setRating(productRatingField.getText());
            productTable.refresh();  // Cập nhật lại bảng sau khi sửa
            clearInputFields();
        } else {
            showAlert("Chọn sản phẩm cần sửa!");
        }
    }

    // Hàm xóa sản phẩm
    @FXML
    private void deleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            productList.remove(selectedProduct);
            clearInputFields();
        } else {
            showAlert("Chọn sản phẩm cần xóa!");
        }
    }

    // Xóa dữ liệu trong các trường nhập
    private void clearInputFields() {
        productIdField.clear();
        productNameField.clear();
        productPriceField.clear();
        productQuantityField.clear();
        productRatingField.clear();
    }

    // Hiển thị cảnh báo
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void goToOrder(ActionEvent event) {
        try {
            // Tải giao diện order.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order.fxml"));
            Parent orderRoot = loader.load();

            // Tạo một Scene mới cho Order
            Scene orderScene = new Scene(orderRoot);

            // Lấy Stage hiện tại từ sự kiện ActionEvent
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(orderScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

