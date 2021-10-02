module com.example.memview {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.memview to javafx.fxml;
    exports com.example.memview;
}