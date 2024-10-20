module com.example.quanlybanhang {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Mở các package cần thiết cho JavaFX FXML
    opens com.example.quanlybanhang.Controller to javafx.fxml;

    // Export các package chứa mã nguồn
    exports com.example.quanlybanhang.Controller;
    exports com.example.quanlybanhang.DataBase;
    exports com.example.quanlybanhang.View;
    opens com.example.quanlybanhang.View to javafx.fxml;
}
