package com.example.memview;

import directory.handling.DirectoryReader;
import galleryUI.GalleryUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloApplication extends Application {

    private static Image firstImage;
    private Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {

        this.mainStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        HelloController controller = fxmlLoader.getController();
        controller.setMainApp(this);

        mainStage.setTitle("MemView");
        mainStage.setScene(scene);

        //Set to start as Maximised Window to prevent weird multiMonitor issues
        mainStage.setMaximized(true);

        mainStage.show();
    }

    public Stage getMainStage() {return this.mainStage;}

    public static <WindowsPath> void main(String[] args) {

        //ToDo get rid of this debugging
        //toDo sort out why the filepath that is being passed to the args array is not translating into a readable file Path for the DirectoryReader class
        /*
        for(String toPrint : args) {
            System.out.println("Test filePath in Loop" + toPrint);
        }
        Path filePath = Paths.get(args[0]).toAbsolutePath();

        System.out.println("File Path given to String[] args: " + filePath.toString());

        firstImage = new Image(filePath.toUri().toString());

         */

        launch();
    }
}