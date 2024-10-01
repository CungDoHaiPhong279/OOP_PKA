package com.example.quanly;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private PasswordField passwordField;

    // Xử lý hành động đăng ký
    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneNumberField.getText().trim();
        String password = passwordField.getText().trim();

        // Kiểm tra xem các trường thông tin có bị để trống không
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            showAlert("Lỗi", "Vui lòng nhập địa chỉ email hợp lệ.");
            return;
        }

        // Kiểm tra định dạng số điện thoại (chỉ chứa các chữ số)
        if (!isValidPhoneNumber(phone)) {
            showAlert("Lỗi", "Số điện thoại phải là số hợp lệ.");
            return;
        }

        // Kiểm tra xem email có tồn tại hay không
        if (isEmailExist(email)) {
            showAlert("Lỗi", "Email đã tồn tại. Vui lòng sử dụng email khác.");
        } else {
            // Lưu người dùng vào cơ sở dữ liệu nếu email chưa tồn tại
            if (registerUser(name, email, phone, password)) {
                showAlert("Thành công", "Đăng ký thành công!");
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Lỗi", "Đăng ký thất bại. Vui lòng thử lại.");
            }
        }
    }

    // Phương thức kiểm tra xem email có tồn tại trong cơ sở dữ liệu hay không
    private boolean isEmailExist(String email) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Nếu COUNT > 0 thì email đã tồn tại
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Phương thức đăng ký người dùng vào cơ sở dữ liệu
    private boolean registerUser(String name, String email, String phone, String password) {
        Connection connection = DBConnection.getConnection();
        String insertQuery = "INSERT INTO users (name, phone_number, email, password, user_role) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password); // Lưu mật khẩu (nên mã hóa mật khẩu)
            preparedStatement.setString(5, "admin"); // Mặc định là admin

            int result = preparedStatement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    // Phương thức kiểm tra định dạng số điện thoại
    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "\\d+";
        return phone.matches(phoneRegex);
    }

    // Phương thức hiển thị cảnh báo
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
