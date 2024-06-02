module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.compiler;
    requires junit;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}