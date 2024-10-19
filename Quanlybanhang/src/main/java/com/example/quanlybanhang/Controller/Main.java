package com.example.quanlybanhang.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Shop Phong Gió Tai!");

        // Tải màn hình đăng nhập ban đầu
        showLoginView();
    }

    public static void main(String[] args) {
        // Tắt tất cả các thông báo logging
        System.setProperty("java.util.logging.ConsoleHandler.level", "OFF");
        Logger.getLogger("").setLevel(Level.OFF);

        launch(args);
    }

    // Phương thức để chuyển đổi giao diện sang màn hình đăng nhập
    public static void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/quanlybanhang/login-register/LoginView.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 607, 442));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể tải LoginView.fxml");
        }
    }

    // Phương thức để chuyển đổi giao diện sang màn hình tạo tài khoản
    public static void showCreateNewAccountView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/quanlybanhang/login-register/CreateNewAccountView.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 607, 442));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể tải CreateNewAccount.fxml");
        }
    }

    // Phương thức để hiển thị thông báo thành công
    public static void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
