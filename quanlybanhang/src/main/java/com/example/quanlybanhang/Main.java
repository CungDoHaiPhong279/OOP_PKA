package com.example.quanlybanhang;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải giao diện từ file FXML login.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/quanlybanhang/login.fxml"));

            // Thiết lập scene và tiêu đề
            Scene scene = new Scene(root);
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);

            // Hiển thị cửa sổ chính
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
