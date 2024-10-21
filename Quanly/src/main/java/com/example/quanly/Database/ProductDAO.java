package com.example.quanly.Database;

import com.example.quanly.Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Lấy tất cả sản phẩm từ cơ sở dữ liệu
    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "SELECT * FROM products";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");
                int supplierId = rs.getInt("supplier_id");
                String createdAt = rs.getString("created_at");

                // Tạo đối tượng Product với các thuộc tính lấy từ cơ sở dữ liệu
                Product product = new Product(productId, productName, description, price, stockQuantity, supplierId, createdAt);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    // Thêm một sản phẩm vào cơ sở dữ liệu
    public static void addProduct(Product product) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "INSERT INTO products (product_name, description, price, stock_quantity, supplier_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setInt(5, product.getSupplierId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin của một sản phẩm trong cơ sở dữ liệu
    public static void updateProduct(Product product) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "UPDATE products SET product_name = ?, description = ?, price = ?, stock_quantity = ?, supplier_id = ? WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setInt(5, product.getSupplierId());
            statement.setInt(6, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa một sản phẩm khỏi cơ sở dữ liệu
    public static void deleteProduct(int productId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "DELETE FROM products WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm một sản phẩm theo ID trong cơ sở dữ liệu
    public static Product getProductById(int productId) {
        Product product = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Quanlybanhang", "root", "123456");
            String query = "SELECT * FROM products WHERE product_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String productName = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");
                int supplierId = rs.getInt("supplier_id");
                String createdAt = rs.getString("created_at");

                // Tạo đối tượng Product với các thuộc tính lấy từ cơ sở dữ liệu
                product = new Product(productId, productName, description, price, stockQuantity, supplierId, createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
}

