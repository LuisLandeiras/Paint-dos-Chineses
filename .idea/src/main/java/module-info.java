module com.example.designprogram {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.designprogram to javafx.fxml;
    exports com.example.designprogram;
}