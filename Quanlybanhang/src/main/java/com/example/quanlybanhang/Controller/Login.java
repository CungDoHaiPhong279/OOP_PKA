package com.example.quanlybanhang.Controller;

import com.example.quanlybanhang.DataBase.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink createAccountLink;

    @FXML
    private Label warningLabel;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        createAccountLink.setOnAction(event -> showCreateNewAccountView());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ thông tin đăng nhập.");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            // Kiểm tra tên người dùng
            String usernameCheckSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement usernameStatement = connection.prepareStatement(usernameCheckSql);
            usernameStatement.setString(1, username);
            ResultSet usernameResult = usernameStatement.executeQuery();

            if (!usernameResult.next()) {
                showAlert(Alert.AlertType.WARNING, "Thông báo", "Tên người dùng không tồn tại.");
                return;
            }

            // Kiểm tra mật khẩu
            String passwordCheckSql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement passwordStatement = connection.prepareStatement(passwordCheckSql);
            passwordStatement.setString(1, username);
            passwordStatement.setString(2, password);
            ResultSet passwordResult = passwordStatement.executeQuery();

            if (passwordResult.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Đăng nhập thành công!");

                // Đóng cửa sổ hiện tại
                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.close();

                // Mở giao diện Home
                loadHomeView();
            } else {
                showAlert(Alert.AlertType.WARNING, "Thông báo", "Sai mật khẩu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Lỗi kết nối cơ sở dữ liệu.");
        }
    }

    private void showCreateNewAccountView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlybanhang/login-register/CreateNewAccountView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Create New Account");

            // Đóng cửa sổ đăng nhập hiện tại
            Stage currentStage = (Stage) createAccountLink.getScene().getWindow();
            currentStage.close();

            // Hiển thị cửa sổ tạo tài khoản
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện tạo tài khoản mới.");
        }
    }

    private void loadHomeView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlybanhang/home/HomeView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở giao diện chính.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
