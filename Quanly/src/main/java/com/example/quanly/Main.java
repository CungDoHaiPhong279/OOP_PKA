package com.example.quanly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            // Tải file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            // Thiết lập Scene với file FXML
            Scene scene = new Scene(root);

            // Thiết lập Stage và hiển thị
            primaryStage.setTitle("Quản lý bán hàng - Đăng nhập");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Tắt tất cả các thông báo logging
        System.setProperty("java.util.logging.ConsoleHandler.level", "OFF");
        Logger.getLogger("").setLevel(Level.OFF);

        launch(args);
    }
}

