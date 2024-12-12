module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.views to javafx.fxml;
    opens com.example.demo.utilities to javafx.fxml;

    exports com.example.demo.controller;
    exports com.example.demo.levels;
    exports com.example.demo.actors;
    exports com.example.demo.views;
    exports com.example.demo.utilities;
}
