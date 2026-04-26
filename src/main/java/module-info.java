module com.example.demo {
    // 必须的JavaFX模块
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base; // 包含javafx.beans，解决Property找不到的问题

    // 开放包给JavaFX反射访问
    opens com.example.demo to javafx.fxml, javafx.graphics;
    opens com.example.demo.model to javafx.base, javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;

    // 导出包
    exports com.example.demo;
    exports com.example.demo.model;
    exports com.example.demo.service;
    exports com.example.demo.controller;
}