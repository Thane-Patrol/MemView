package com.example.memview;

import directory.handling.DirectoryReader;
import galleryUI.GalleryUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {

    private static Rectangle2D screenBounds;

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        /*
        System.out.println("Give a file path");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

         */

        //Todo get the filename in a whitespace trimmed format to pass as a constructor to the DirectoryReader class
        //Create DirectoryReader to index files before the GalleryUI is started
        DirectoryReader directoryReader = new DirectoryReader("D:/javaMemView/gtest(1).jpg");

        //Create new GalleryUI on application start
        GalleryUI galleryUI = new GalleryUI(directoryReader.getPath());

        Button nextButton = galleryUI.getNextButton();
        Button backButton = galleryUI.getBackButton();

        new AnimationTimer() {

            @Override
            public void handle(long now) {

                nextButton.setOnMouseClicked(event -> {
                    Image image = new Image(directoryReader.getNextImage().toUri().toString());
                    galleryUI.setNextImageOnButtonPress(image);
                });

                backButton.setOnMouseClicked(event -> {
                    Image image = new Image(directoryReader.getPreviousImage().toUri().toString());
                    galleryUI.setPreviousImageOnButtonPress(image);
                });
            }
        }.start();


        stage.setTitle("Hello!");
        stage.setScene(galleryUI.getScene());

        //Set to start as Maximised Window to prevent weird multiMonitor issues
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}