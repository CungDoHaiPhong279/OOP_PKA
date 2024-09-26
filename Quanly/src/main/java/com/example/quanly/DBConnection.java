package com.example.quanly;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Quanlybanhang";
    private static final String USER = "root"; // Tên người dùng MySQL của bạn
    private static final String PASSWORD = "123456"; // Mật khẩu MySQL của bạn

    private static Connection connection = null;

    // Phương thức kết nối
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to the database successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the database.");
            }
        }
        return connection;
    }
}
