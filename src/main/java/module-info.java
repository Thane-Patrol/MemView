module com.example.memview {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires metadata.extractor;
    requires org.apache.commons.io;
    requires java.prefs;
    requires thumbnailator;

    //todo Fix pom.xml by adding exclusions for packages that contain bits of javaFX dependencies, see: https://youtrack.jetbrains.com/issue/IDEA-171320

    opens com.example.memview to javafx.fxml;
    exports com.example.memview;
}