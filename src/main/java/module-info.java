module com.example.memview {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires metadata.extractor;
    requires org.apache.commons.io;
    requires java.prefs;

    opens com.example.memview to javafx.fxml;
    exports com.example.memview;
}