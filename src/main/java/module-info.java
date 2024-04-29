module com.example.wealth_creation_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.wealth_creation_system to javafx.fxml;
    exports com.example.wealth_creation_system;
}