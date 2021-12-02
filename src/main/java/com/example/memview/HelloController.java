package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class HelloController {
    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView mainImageView;
    @FXML
    private ImageView zoomBoxView;
    @FXML
    private VBox zoomBoxContainer;

    @FXML
    private StackPane root;

    private DirectoryReader directoryReader;

    //Tracks the last key press to ensure key press isn't registered when key is held
    private KeyCode eventTracker = null;

    //Used to store screenBounds dimensions as a variable to automatically resize imageView components to suit monitor
    private Rectangle2D screenBounds;

    private ScrollEvent scrollEventTracker = null;

    //Used to allow reading of metadata, read multiple images in the background, independent of user interaction
    private ImageReader imageReader;

    //Used for keeping track of the amount of level of scrolling for zoom calculations
    private double scrollAmountTracker = 0.0;

    public HelloController() {

        root = new StackPane();

        String directory = "D:\\javaMemView\\1.jpg";
        System.out.println("Directory: " + directory);

        directoryReader = new DirectoryReader(directory);

        mainImageView = new ImageView();
        zoomBoxView = new ImageView();
        zoomBoxContainer = new VBox();

        screenBounds = Screen.getPrimary().getVisualBounds();

    }

    public Button getNextButton() {
        return this.nextButton;
    }

    public Button getBackButton() {
        return this.backButton;
    }

    @FXML
    private void initialize() throws IOException {
        //Creating a bufferedImage first as the Twelve Monkeys library creates buffered images
        BufferedImage imageSwing = ImageIO.read(directoryReader.getCurrentImage().toFile());
        Image imageFX = SwingFXUtils.toFXImage(imageSwing, null);
        mainImageView.setImage(imageFX);

        resizeImageForScreen(mainImageView);

        //todo make the binding property work with any size/ resolution photo
        //mainImageView.fitHeightProperty().bind(root.widthProperty());
        mainImageView.fitWidthProperty().bind((root.widthProperty()));
        zoomBoxView.setVisible(false);
    }

    @FXML
    public void nextButtonAction() throws IOException{
        File nextImageFilePath = directoryReader.getNextImage().toFile();
        Image nextImage = SwingFXUtils.toFXImage(ImageIO.read(nextImageFilePath), null);
        mainImageView.setImage(nextImage);
    }

    @FXML
    public void backButtonAction() throws IOException {
        File previousImageFilePath = directoryReader.getPreviousImage().toFile();
        Image previousImage = SwingFXUtils.toFXImage(ImageIO.read(previousImageFilePath), null);
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
    public void keyPressHandler(KeyEvent event) throws IOException {

        if(event.getCode() == KeyCode.CONTROL) {
            eventTracker = event.getCode();
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
    private void scrollHandler(ScrollEvent scrollEvent) throws IOException{
        scrollEventTracker = scrollEvent;
        //scrollAmountTracker used to keep track of different stages of zoom.

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

    //Trigger should be on mouseclick on the ImageView object itself
    @FXML
    private void createZoomBoxOnClick(MouseEvent event) throws IOException{
        zoomBoxView.setVisible(true);
        double xCoordinates = event.getSceneX();
        double yCoordinates = event.getSceneY();
        //Pos xyCoordinates = new Pos(yCoordinates, xCoordinates);

        //Manipulation of zoomBoxContainer with the objective to sit on top of the mainImageView and show the zoomed section underneath
        //todo implement the above functionality
        zoomBoxContainer.setAlignment(Pos.CENTER);
        zoomBoxContainer.toFront();
        zoomBoxContainer.setOpacity(0);

        System.out.println("method createZoomBoxOnClick called");
        System.out.println("X coordinates: " + xCoordinates);
        System.out.println("Y coordinates: " + yCoordinates);

        zoomBoxView.setImage(SwingFXUtils.toFXImage(ImageIO.read(directoryReader.getCurrentImage().toFile()), null));
        zoomBoxView.setScaleX(5);
        zoomBoxView.setScaleY(5);


    }

    @FXML
    private void keyReleasedHandler(KeyEvent event) {
        eventTracker = null;
    }

    private HelloApplication mainApp;

    public void setMainApp(HelloApplication mainApp) {this.mainApp = mainApp;}
}