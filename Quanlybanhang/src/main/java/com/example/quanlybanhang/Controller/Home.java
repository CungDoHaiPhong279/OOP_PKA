package com.example.quanlybanhang.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class Home {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnGoods; // Thêm nút hàng hóa

    @FXML
    public void initialize() {
        // Thêm sự kiện vào nút Logout
        btnLogout.setOnAction(event -> handleLogout());

        // Thêm sự kiện vào nút Hàng hóa
        btnGoods.setOnAction(event -> handleProductView());
    }

    private void handleLogout() {
        try {
            // Đóng cửa sổ hiện tại
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.close();

            // Mở lại màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlybanhang/login-register/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleProductView() {
        try {
            // Đóng cửa sổ hiện tại
            Stage stage = (Stage) btnGoods.getScene().getWindow();
            stage.close();

            // Mở màn hình ProductView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlybanhang/products/ProductsView.fxml"));
            Parent root = loader.load();
            Stage productStage = new Stage();
            productStage.setTitle("Products");
            productStage.setScene(new Scene(root));
            productStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
