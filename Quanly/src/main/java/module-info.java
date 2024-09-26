module com.example.quanly {
    requires java.sql;
    // Khai báo các module khác mà bạn cần sử dụng
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.quanly to javafx.fxml;
    exports com.example.quanly;
}
