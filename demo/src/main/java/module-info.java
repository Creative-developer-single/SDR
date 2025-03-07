module com.example.sdr {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.web;
    requires jmatio;

    opens com.example.sdr to javafx.fxml;
    exports com.example.sdr;
}
