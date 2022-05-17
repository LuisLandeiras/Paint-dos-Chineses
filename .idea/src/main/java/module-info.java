module com.example.designprogram {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires transitive java.desktop;

    opens com.example.designprogram to javafx.fxml;
    exports com.example.designprogram;
}