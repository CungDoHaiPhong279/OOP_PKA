package com.example.quanlybanhang.Controller;

import com.example.quanlybanhang.DataBase.DBConnection;
import com.example.quanlybanhang.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Products {

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> retailPriceColumn;

    @FXML
    private TableColumn<Product, Double> costPriceColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    @FXML
    private TableColumn<Product, Integer> soldColumn;

    @FXML
    private TableColumn<Product, String> expiryDateColumn;

    private ObservableList<Product> productList;

    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        retailPriceColumn.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        costPriceColumn.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        // Load data from database
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        productList = FXCollections.observableArrayList();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("retailPrice"),
                        rs.getDouble("costPrice"),
                        rs.getInt("stock"),
                        rs.getInt("sold"),
                        rs.getString("expiryDate")
                );
                productList.add(product);
            }
            System.out.println("Product List: " + productList);
            productTable.setItems(productList);
            productTable.refresh();
            System.out.println(productList);
            productTable.setItems(productList);

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
