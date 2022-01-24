package com.example.memview;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private Stage mainStage;
    private static HostServices hostServices;
    private Stage photoConversionStage;

    @Override
    public void start(Stage stage) throws IOException {

        this.mainStage = new Stage();

        //main FXML file
        FXMLLoader fxmlLoaderMain = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoaderMain.load(), 640, 480);
        MainController controller = fxmlLoaderMain.getController();
        controller.setMainApp(this);
        controller.setHostServices(this.getHostServices());



        mainStage.setTitle("MemView");
        mainStage.setScene(scene);

        //Set to start as Maximised Window to prevent weird multiMonitor issues
        mainStage.setMaximized(true);
        mainStage.show();
        scene.getRoot().requestFocus();

        //popup FXML file for conversion of photos
        //This needs to be called before the main controller
        Stage popupStage = new Stage();
        FXMLLoader fxmlLoaderPopupConversion = new FXMLLoader(HelloApplication.class.getResource("photo-conversion.fxml"));
        Scene popupScene = new Scene(fxmlLoaderPopupConversion.load(), 640, 480);
        PhotoConversionPopupController photoConversionPopupController = fxmlLoaderPopupConversion.getController();

        photoConversionPopupController.setMainController(controller);
        photoConversionPopupController.setMainStage(mainStage);
        controller.setPhotoConversionPopupController(photoConversionPopupController);

        //popup.getContent().add(popupScene.getRoot());
        //photoConversionPopupController.setPopup(popup);
        popupStage.setScene(popupScene);
        photoConversionPopupController.setPopupStage(popupStage);
        popupStage.setTitle("Photo Resizing");


    }

    public Stage getMainStage() {return this.mainStage;}

    public static void main(String[] args) {

        launch();
    }
}