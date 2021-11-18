package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    //Tracks the last keypress to ensure keypress isn't registered when key is held
    private KeyCode eventTracker = null;

    public HelloController() {

        root = new StackPane();

        String directory = "D:\\javaMemView\\gtest(1).jpg";
        System.out.println("Directory: " + directory);

        directoryReader = new DirectoryReader(directory);

        mainImageView = new ImageView();

    }

    public Button getNextButton() {
        return this.nextButton;
    }

    public Button getBackButton() {
        return this.backButton;
    }

    @FXML
    private void initialize() {

        mainImageView.setImage(new Image(directoryReader.getCurrentImage().toUri().toString()));

        mainImageView.fitHeightProperty().bind(root.widthProperty());
        mainImageView.fitWidthProperty().bind(root.widthProperty());
    }

    @FXML
    public void nextButtonAction() {
        Path nextImagePath = directoryReader.getNextImage();
        Image nextImage = new Image(nextImagePath.toUri().toString(), 1080, 720, true, true);

        mainImageView.setImage(nextImage);
    }

    @FXML
    public void backButtonAction() {
        Path previousImagePath = directoryReader.getPreviousImage();
        Image previousImage = new Image(previousImagePath.toUri().toString(), 1080, 720, true, true);
        mainImageView.setImage(previousImage);
    }

    @FXML
    public void keyPressHandler(KeyEvent event) {
        System.out.println("Keypress: " + event.getCode());

        if(event.getCode() == KeyCode.RIGHT && eventTracker == null) {
            nextButtonAction();
            eventTracker = event.getCode();

        } else if (event.getCode() == KeyCode.LEFT && eventTracker == null) {
            backButtonAction();
            eventTracker = event.getCode();

        } else {
            event.consume();
        }
    }

    @FXML
    private void keyReleasedHandler(KeyEvent event) {
        eventTracker = null;
    }

    private HelloApplication mainApp;

    public void setMainApp(HelloApplication mainApp) {this.mainApp = mainApp;}
}