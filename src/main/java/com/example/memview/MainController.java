package com.example.memview;

import com.drew.lang.GeoLocation;
import directory.handling.DirectoryReader;
import directory.handling.FileHandling;
import javafx.application.HostServices;
import javafx.fxml.FXML;
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
import preferences.UserPreferences;

import javax.imageio.ImageReader;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MainController {

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

    //Class used for handling metadata
    private MetadataWrangler metadataWrangler;

    //Class used for user preferences
    private UserPreferences userPreferences;
    //Class used for opening directory
    private FileHandling fileHandling;

    //Label to display the metadata
    @FXML
    private Label metadataLabel;

    @FXML
    private MenuBar menuBar;

    private PhotoConversionPopupController photoConversionPopupController;

    private GeoLocation geoLocation;
    private HostServices hostServices;

    //Used for keeping track of the amount of level of scrolling for zoom calculations
    private double scrollAmountTracker = 0.0;

    public MainController() {

        root = new StackPane();

        String directory =  "/home/hugh/Documents/Development/javaMemView/1.png";  // //"D:\\javaMemView\\1.jpg"; // /Users/hugh/Desktop/memview/Back Garden From stairs.png
        System.out.println("Directory: " + directory);

        //Instantiation of all the helper classes needed, Order is important
        directoryReader = new DirectoryReader(directory);
        conversionLogicClass = new ConversionLogic(directoryReader);
        userPreferences = new UserPreferences();
        applicationLogic = new PhotoViewerApplicationLogic(directoryReader, this);
        metadataWrangler = new MetadataWrangler(userPreferences, applicationLogic);
        fileHandling = new FileHandling();

        mainImageView = new ImageView();
        zoomBoxView = new ImageView();
        zoomBoxContainer = new Pane();
        metadataLabel = new Label();

        screenBounds = Screen.getPrimary().getVisualBounds();
        galleryThumbnailParentToolbar = new ToolBar();
        scrollPaneRootFileRibbon = new ScrollPane();
        thumbnailContainerRibbon = new HBox();


    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void showConversionPopup() {
        photoConversionPopupController.showPopup();
    }

    public void setPhotoConversionPopupController(PhotoConversionPopupController photoConversionPopupController) {
        this.photoConversionPopupController = photoConversionPopupController;

        //This needs to be called here to prevent NPE
        photoConversionPopupController.setConversionLogic(conversionLogicClass);
    }

    @FXML
    private void initialize() throws IOException {

        //Creating a bufferedImage first as the Twelve Monkeys library creates buffered images unless a GIF then use native Image object
        File firstImagePath = directoryReader.getCurrentImage().toFile();
        Image firstImage = directoryReader.loadImage();


        mainImageView.setImage(firstImage);
        metadataLabel.setAlignment(Pos.TOP_LEFT);
        metadataLabel.toFront();
        getImageMetadata(firstImagePath.toPath());

        resizeImageForScreen(mainImageView);

        //buttonHolder.toFront();
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

        Image nextImage = directoryReader.loadImage();
        mainImageView.setImage(nextImage);
    }

    @FXML
    public void backButtonAction() throws IOException {
        Path previousImagePath = directoryReader.getPreviousImage();
        getImageMetadata(previousImagePath);
        File previousImageFilePath = previousImagePath.toFile();

        Image previousImage = directoryReader.loadImage();

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
        metadataLabel.toFront();
        metadataLabel.setText(metadataWrangler.setMetadataLabel(imageFile));
    }

    //This method must always be called after the getImageMetadata method
    @FXML
    private void openGoogleMapsGPS() {
        if(metadataWrangler.getGPSMetadataForNullBoolean()) {
            String url = metadataWrangler.getGoogleMapsURL();
            hostServices.showDocument(url);
        }
        return;
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

    public void updateMetadataLabel(Path imagePath) throws IOException {
        getImageMetadata(imagePath);
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

    //todo Make sure radio buttons reflect the preferences file
    @FXML
    private void toggleGPSExifPreferences() throws IOException {
        //If currently set to true, set to false - default is false
        if(userPreferences.getMetadataGPSLabel()) {
            userPreferences.setMetadataGPSLabel(false);
            getImageMetadata(directoryReader.getCurrentImage());
        } else {
            userPreferences.setMetadataGPSLabel(true);
            getImageMetadata(directoryReader.getCurrentImage());
        }
    }

    @FXML
    private void toggleFileSizePreferences() throws IOException {
        if(userPreferences.getMetadataFileSizeLabel()) {
            userPreferences.setMetadataFileSizeLabel(false);
            getImageMetadata(directoryReader.getCurrentImage());
        } else {
            userPreferences.setMetadataFileSizeLabel(true);
            getImageMetadata(directoryReader.getCurrentImage());
        }
    }

    @FXML
    private void toggleCreationDatePreferences() throws IOException {
        if(userPreferences.getMetadataCreationLabel()) {
            userPreferences.setMetadataCreationLabel(false);
            getImageMetadata(directoryReader.getCurrentImage());
        } else {
            userPreferences.setMetadataCreationLabel(true);
            getImageMetadata(directoryReader.getCurrentImage());
        }
    }

    public FileHandling getFileHandling() {
        return fileHandling;
    }
}