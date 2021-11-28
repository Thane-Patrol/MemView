package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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

    //Used to store screenBounds dimensions as a variable to automatically resize imageView components to suit monitor
    private Rectangle2D screenBounds;

    private ScrollEvent scrollEventTracker = null;

    //Used for snapping the zoom back to default when key is released
    private boolean controlKeypressTracker = false;
    //Used for keeping track of the amount of level of scrolling for zoom calculations
    private double scrollAmountTracker = 0.0;

    public HelloController() {

        root = new StackPane();

        String directory = "D:\\javaMemView\\gtest(1).jpg";
        System.out.println("Directory: " + directory);

        directoryReader = new DirectoryReader(directory);

        mainImageView = new ImageView();

        screenBounds = Screen.getPrimary().getVisualBounds();

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
        resizeImageForScreen(mainImageView);

        //DoubleProperty rootPaneProperty

        //todo make the binding property work with any size/ resolution photo
        //mainImageView.fitHeightProperty().bind(root.widthProperty());
        mainImageView.fitWidthProperty().bind((root.widthProperty()));
    }

    @FXML
    public void nextButtonAction() {
        Path nextImagePath = directoryReader.getNextImage();
        Image nextImage = new Image(nextImagePath.toUri().toString());
        //resizeImageForScreen(mainImageView);
        mainImageView.setImage(nextImage);
    }

    @FXML
    public void backButtonAction() {
        Path previousImagePath = directoryReader.getPreviousImage();
        Image previousImage = new Image(previousImagePath.toUri().toString());
        //resizeImageForScreen(mainImageView);
        mainImageView.setImage(previousImage);
    }

    public ImageView resizeImageForScreen(ImageView imageView) {
        imageView.setPreserveRatio(true);

        System.out.println("Height: " + imageView.getFitHeight());

        //imageView.setFitWidth(screenBounds.getWidth());
        imageView.setFitHeight(screenBounds.getHeight() - 100);
        return imageView;
    }

    @FXML
    public void keyPressHandler(KeyEvent event) {

        if(event.getCode() == KeyCode.CONTROL) {
            eventTracker = event.getCode();
            controlKeypressTracker = true;
        }

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
    private void scrollHandler(ScrollEvent scrollEvent) {
        scrollEventTracker = scrollEvent;
        //scrollAmountTracker used to keep track of different stages of zoom.

        System.out.println("Scroll amount tracker: " + scrollAmountTracker);
        System.out.println(".getDeltaY() result: " + scrollEvent.getDeltaY());

        if (eventTracker == KeyCode.CONTROL && scrollEvent.getDeltaY() > 0 && scrollAmountTracker > 39) {
            zoomInActionSecondLevel();
            scrollAmountTracker += scrollEvent.getDeltaY();

        } else if (eventTracker == KeyCode.CONTROL && scrollEvent.getDeltaY() < 0 && scrollAmountTracker < -41) {
            zoomOutActionSecondLevel();
            scrollAmountTracker += scrollEventTracker.getDeltaY();

        } else if (eventTracker == KeyCode.CONTROL && scrollEvent.getDeltaY() < 0 && scrollAmountTracker > -40) {

            scrollAmountTracker += scrollEventTracker.getDeltaY();
            zoomOutActionFirstLevel();

        } else if (eventTracker == KeyCode.CONTROL && scrollEvent.getDeltaY() > 0 && scrollAmountTracker < 40) {

            scrollAmountTracker += scrollEvent.getDeltaY();
            zoomInActionFirstLevel();

        } else if (scrollEvent.getDeltaY() < 0) {
            backButtonAction();
        } else if (scrollEvent.getDeltaY() > 0) {
            nextButtonAction();
        }
        scrollEvent.consume();
    }

    private void zoomInActionFirstLevel() {
        mainImageView.setScaleY(2);
        mainImageView.setScaleX(2);
    }

    private void zoomOutActionFirstLevel() {
        mainImageView.setScaleY(1);
        mainImageView.setScaleX(1);
        scrollAmountTracker = 0;
    }

    private void zoomInActionSecondLevel() {
        mainImageView.setScaleY(4);
        mainImageView.setScaleX(4);
    }

    private void zoomOutActionSecondLevel() {
        mainImageView.setScaleY(2);
        mainImageView.setScaleX(2);
        scrollAmountTracker = 40;
    }

    @FXML
    private void keyReleasedHandler(KeyEvent event) {
        eventTracker = null;
        controlKeypressTracker = false;
    }

    private HelloApplication mainApp;

    public void setMainApp(HelloApplication mainApp) {this.mainApp = mainApp;}
}