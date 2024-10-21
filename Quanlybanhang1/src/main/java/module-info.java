module com.example.quanly {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    // Export các package để sử dụng trong các file FXML
    exports com.example.quanly.Controller;
    exports com.example.quanly.Model;
    exports com.example.quanly.View;

    // Mở các package cho JavaFX FXML sử dụng reflection để tải FXML
    opens com.example.quanly.Controller to javafx.fxml;
    opens com.example.quanly.View to javafx.fxml;
}
