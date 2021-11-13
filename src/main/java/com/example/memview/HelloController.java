package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class HelloController {
    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView mainImageView;

    @FXML
    private StackPane root;

    private DirectoryReader directoryReader;

    public HelloController() {

        root = new StackPane();

        String directory = "D:\\javaMemView\\gtest(1).jpg";
        System.out.println("Directory: " + directory);

        directoryReader = new DirectoryReader(directory);

        mainImageView = new ImageView();




    };

    @FXML
    private void initialize() {

        mainImageView.setImage(new Image(directoryReader.getCurrentImage().toUri().toString()));

        mainImageView.fitHeightProperty().bind(root.widthProperty());
        mainImageView.fitWidthProperty().bind(root.widthProperty());
    }

    @FXML
    private void nextButtonAction() {
        Path nextImage = directoryReader.getNextImage();
        mainImageView.setImage(new Image((nextImage).toUri().toString()));
    }

    @FXML
    private void backButtonAction() {
        Image previousImage = new Image(directoryReader.getPreviousImage().toUri().toString());
        mainImageView.setImage(previousImage);
    }

    private HelloApplication mainApp;

    public void setMainApp(HelloApplication mainApp) {this.mainApp = mainApp;}
}