module com.example.memview {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.example.memview to javafx.fxml;
    exports com.example.memview;
}