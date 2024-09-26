package com.example.quanlybanhang;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Xử lý sự kiện khi nhấn nút đăng nhập
    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Kiểm tra tài khoản và mật khẩu (giả lập)
        if ("admin".equals(username) && "123456".equals(password)) {
            // Đăng nhập thành công, chuyển sang giao diện quản lý sản phẩm
            switchToProductManagement();
        } else {
            // Đăng nhập thất bại, hiển thị thông báo lỗi
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect Username or Password");
        }
    }

    // Hàm hiển thị cảnh báo hoặc thông báo
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hàm chuyển sang giao diện quản lý sản phẩm
    private void switchToProductManagement() {
        try {
            // Lấy cửa sổ hiện tại (Stage) từ trường usernameField
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Tải file FXML của giao diện quản lý sản phẩm
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/quanlybanhang/product.fxml")));

            // Kiểm tra xem root có null không
            if (root == null) {
                throw new RuntimeException("product.fxml not found or not loaded correctly.");
            }

            // Thiết lập scene mới cho stage
            stage.setScene(new Scene(root));
            stage.setTitle("Quản Lý Sản Phẩm");
        } catch (Exception e) {
            // In chi tiết lỗi ra console
            e.printStackTrace();

            // Hiển thị thông báo lỗi cho người dùng
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the product management interface. " +
                    "Please check the console for more details.");
        }
    }
}
