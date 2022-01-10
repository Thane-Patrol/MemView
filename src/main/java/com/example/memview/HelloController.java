package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import photo.conversion.ConversionLogic;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;

public class HelloController {

    @FXML
    private ImageView mainImageView;
    @FXML
    private ImageView zoomBoxView;
    @FXML
    private Pane zoomBoxContainer;
    @FXML
    private ToolBar galleryThumbnailParentToolbar;
    @FXML
    private ScrollPane scrollPaneRootFileRibbon;
    @FXML
    private HBox thumbnailContainerRibbon;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private HBox buttonHolder;
    @FXML
    private Image zoomedImage;

    @FXML
    private StackPane root;

    private final DirectoryReader directoryReader;

    //Tracks the last key press to ensure key press isn't registered when key is held
    private KeyCode eventTracker = null;

    //Used to store screenBounds dimensions as a variable to automatically resize imageView components to suit monitor
    private final Rectangle2D screenBounds;

    private ScrollEvent scrollEventTracker = null;

    //Used to allow reading of metadata, read multiple images in the background, independent of user interaction
    private ImageReader imageReader;

    //Used for application logic and data handling to make this method a bit cleaner
    private PhotoViewerApplicationLogic applicationLogic;

    //Class used to handle the logic of photo resizing, cropping, etc
    private ConversionLogic conversionLogicClass;

    //Label to display the metadata
    @FXML
    private Label metadataLabel;

    //Used for keeping track of the amount of level of scrolling for zoom calculations
    private double scrollAmountTracker = 0.0;

    public HelloController() {

        root = new StackPane();

        String directory =  "D:\\javaMemView\\1.jpg"; //"D:\\javaMemView\\1.jpg"; // /Users/hugh/Desktop/memview/Back Garden From stairs.png
        System.out.println("Directory: " + directory);

        directoryReader = new DirectoryReader(directory);

        conversionLogicClass = new ConversionLogic(directoryReader);


        mainImageView = new ImageView();
        zoomBoxView = new ImageView();
        zoomBoxContainer = new Pane();
        metadataLabel = new Label();

        screenBounds = Screen.getPrimary().getVisualBounds();
        applicationLogic = new PhotoViewerApplicationLogic(directoryReader, this);
        galleryThumbnailParentToolbar = new ToolBar();
        scrollPaneRootFileRibbon = new ScrollPane();
        thumbnailContainerRibbon = new HBox();

    }

    @FXML
    private void initialize() throws IOException {
        //Creating a bufferedImage first as the Twelve Monkeys library creates buffered images
        BufferedImage imageSwing = ImageIO.read(directoryReader.getCurrentImage().toFile());
        Image imageFX = SwingFXUtils.toFXImage(imageSwing, null);
        mainImageView.setImage(imageFX);
        metadataLabel.setAlignment(Pos.TOP_LEFT);

        resizeImageForScreen(mainImageView);

        buttonHolder.toFront();
        galleryThumbnailParentToolbar.setOpacity(0.0);
        galleryThumbnailParentToolbar.toFront();
        applicationLogic.addPhotoThumbnailsToHBox(thumbnailContainerRibbon);

        thumbnailContainerRibbon.setSpacing(75);

        scrollPaneRootFileRibbon.setFitToWidth(true);
        scrollPaneRootFileRibbon.setPrefHeight(applicationLogic.getVboxHeight());
        scrollPaneRootFileRibbon.setPrefWidth(screenBounds.getWidth() - 100);
        
        //todo make the binding property work with any size/ resolution photo
        //mainImageView.fitHeightProperty().bind(root.widthProperty());
        mainImageView.fitWidthProperty().bind((root.widthProperty()));
        zoomBoxContainer.setOpacity(0.0);
        zoomBoxContainer.setScaleX(mainImageView.getScaleX() / 4);
        zoomBoxContainer.setScaleY(mainImageView.getScaleY() / 4);
    }

    @FXML
    public void nextButtonAction() throws IOException{
        Path nextImageFilePath = directoryReader.getNextImage();
        getImageMetadata(nextImageFilePath);
        File nextImageFile = nextImageFilePath.toFile();
        Image nextImage = SwingFXUtils.toFXImage(ImageIO.read(nextImageFile), null);
        mainImageView.setImage(nextImage);
    }

    @FXML
    public void backButtonAction() throws IOException {
        Path previousImagePath = directoryReader.getPreviousImage();
        getImageMetadata(previousImagePath);
        File previousImageFilePath = previousImagePath.toFile();
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

    public void setTransparentToolbar() {
        galleryThumbnailParentToolbar.setOpacity(0.0);
    }

    public void setVisibleToolbar() {
        galleryThumbnailParentToolbar.setOpacity(100);
        galleryThumbnailParentToolbar.toFront();
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

    public void getImageMetadata(Path imageFile) throws IOException{
        BasicFileAttributes fileAttributes = Files.readAttributes(imageFile, BasicFileAttributes.class);
        metadataLabel.setText("Creation: " + fileAttributes.creationTime().toInstant() + " Size: " + applicationLogic.getPhotoSizeInUnits(imageFile));
    }

    @FXML
    private void scrollHandler(ScrollEvent scrollEvent) throws IOException{
        scrollEventTracker = scrollEvent;
        //scrollAmountTracker used to keep track of different stages of zoom.

        boolean controlKeyPressed = eventTracker == KeyCode.CONTROL;
        double scrollEventAmount = scrollEvent.getDeltaY();

        if (controlKeyPressed && scrollEventAmount > 0 && scrollAmountTracker > 39) {
            zoomInActionSecondLevel();
            scrollAmountTracker += scrollEventAmount;

        } else if (controlKeyPressed && scrollEventAmount < 0 && scrollAmountTracker < -41) {
            zoomOutActionSecondLevel();
            scrollAmountTracker += scrollEventTracker.getDeltaY();

        } else if (controlKeyPressed && scrollEventAmount < 0 && scrollAmountTracker > -40) {
            scrollAmountTracker += scrollEventTracker.getDeltaY();
            zoomOutActionFirstLevel();

        } else if (controlKeyPressed && scrollEventAmount > 0 && scrollAmountTracker < 40) {
            scrollAmountTracker += scrollEventAmount;
            zoomInActionFirstLevel();

        } else if (scrollEventAmount < 0) {
            backButtonAction();

        } else if (scrollEventAmount > 0) {
            nextButtonAction();
        }
        scrollEvent.consume();
    }

    //todo scroll in on the section the cursor is over, not the center
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
    private void createZoomBoxOnClick(MouseEvent event){
        zoomBoxContainer.setOpacity(100);
        double xCoordinates = event.getSceneX();
        double yCoordinates = event.getSceneY();
        //Pos xyCoordinates = new Pos(yCoordinates, xCoordinates);

        //Manipulation of zoomBoxContainer with the objective to sit on top of the mainImageView and show the zoomed section underneath
        //todo implement the above functionality
        zoomedImage = mainImageView.getImage();

        zoomBoxView.setImage(mainImageView.getImage());
        zoomBoxView.setScaleX(10);
        zoomBoxView.setScaleY(10);
    }

    @FXML
    private void moveZoomBoxWithMouse(MouseEvent event) {
        if(zoomBoxContainer.getOpacity() == 100) {

            double mouseXCoordinates = event.getSceneX();
            double mouseYCoordinates = event.getSceneY();

            zoomBoxContainer.setTranslateX(mouseXCoordinates - zoomBoxContainer.getWidth()/3);
            zoomBoxContainer.setTranslateY(mouseYCoordinates - zoomBoxContainer.getHeight()/3);

            zoomBoxView.setImage(applicationLogic.getImageUnderneathZoomBoxContainer(zoomBoxContainer, mainImageView, event));

            //todo Make the zoombox actually zooms in, not just providing a smaller version of the main image
        }
    }


    public void gotoImageOnClick(Image imageToGoTo) {
        mainImageView.setImage(imageToGoTo);
    }

    @FXML
    private void hideZoomBoxOnRelease(MouseEvent event) {
        zoomBoxContainer.setOpacity(0.0);
    }

    @FXML
    private void keyReleasedHandler(KeyEvent event) {
        eventTracker = null;
    }

    private HelloApplication mainApp;

    public void setMainApp(HelloApplication mainApp) {this.mainApp = mainApp;}

    public DirectoryReader getDirectoryReader() {
        return directoryReader;
    }

}