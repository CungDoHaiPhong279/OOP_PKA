package com.example.quanly;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Xử lý hành động đăng nhập
    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("Attempting login with email: " + email);

        // Kiểm tra sự tồn tại của người dùng
        if (!isUserExist(email)) {
            showAlert("Lỗi", "Người dùng không tồn tại.");
        } else {
            // Kiểm tra mật khẩu
            if (isPasswordCorrect(email, password)) {
                System.out.println("Login successful!");
                // Chuyển đến giao diện chính của ứng dụng sau khi đăng nhập thành công
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) usernameField.getScene().getWindow();  // Lấy Stage hiện tại
                    stage.setTitle("Quản lý sản phẩm");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Lỗi", "Sai mật khẩu.");
            }
        }
    }
    // Phương thức kiểm tra sự tồn tại của người dùng từ cơ sở dữ liệu
    private boolean isUserExist(String email) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Phương thức kiểm tra mật khẩu từ cơ sở dữ liệu
    private boolean isPasswordCorrect(String email, String password) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT password FROM users WHERE email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String correctPassword = resultSet.getString("password");
                return correctPassword.equals(password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Phương thức hiển thị cảnh báo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Phương thức để mở giao diện tạo tài khoản và quay lại trang đăng nhập sau khi đóng
    @FXML
    private void openCreateAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create Account");
            stage.setScene(new Scene(root));

            // Đợi cho đến khi người dùng đóng cửa sổ tạo tài khoản
            stage.showAndWait();

            // Quay lại trang đăng nhập sau khi tạo tài khoản
            System.out.println("Quay lại trang đăng nhập sau khi tạo tài khoản.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

