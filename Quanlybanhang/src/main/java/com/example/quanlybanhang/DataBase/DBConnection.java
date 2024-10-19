package com.example.quanlybanhang.DataBase;

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
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Nạp driver
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy MySQL Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Kết nối cơ sở dữ liệu thất bại.");
            e.printStackTrace();
        }
        return connection;
    }

}
