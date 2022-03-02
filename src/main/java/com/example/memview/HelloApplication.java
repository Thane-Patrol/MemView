package com.example.memview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Stage mainStage = new Stage();

        //main FXML file
        FXMLLoader fxmlLoaderMain = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoaderMain.load(), 640, 480);
        PhotoViewerController controller = fxmlLoaderMain.getController();
        controller.setHostServices(this.getHostServices());

        //popup FXML file for conversion of photos
        //This needs to be called before the main controller
        Stage popupStage = new Stage();
        FXMLLoader fxmlLoaderPopupConversion = new FXMLLoader(HelloApplication.class.getResource("photo-conversion.fxml"));
        Scene popupScene = new Scene(fxmlLoaderPopupConversion.load(), 640, 800);
        PhotoConversionController photoConversionController = fxmlLoaderPopupConversion.getController();

        //These two methods need to be called in this order
        controller.setPhotoConversionPopupController(photoConversionController);
        controller.setServicesForPhotoController();

        popupStage.setScene(popupScene);
        photoConversionController.setPopupStage(popupStage);
        popupStage.setTitle("Photo Resizing");

        mainStage.setTitle("MemView");
        mainStage.setScene(scene);

        //Set to start as Maximised Window to prevent weird multiMonitor issues
        mainStage.setMaximized(true);
        mainStage.show();
        scene.getRoot().requestFocus();
    }

    public static void main(String[] args) {

        launch();
    }
}