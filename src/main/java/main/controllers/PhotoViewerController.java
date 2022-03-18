/*
 * MemView - a simple photo viewer and converter written in Java
 *     Copyright (C) 2021 Hugh Mandalidis
 *     Contact: mandalidis.hugh@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>
 */

package main.controllers;

import directory.handling.DirectoryReader;
import directory.handling.FileHandling;
import directory.handling.ImageAndPathHolder;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
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
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PhotoViewerController {

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
    private CheckMenuItem creationDateCheckMenu;
    @FXML
    private CheckMenuItem fileSizeCheckMenu;
    @FXML
    private CheckMenuItem GPSCheckMenu;
    @FXML
    private StackPane root;
    //Path of the outofBoundsImage
    private final Path outOfBoundsPath = Paths.get("src/main/resources/image.Resources/41nrqdLzutL._AC_SY580_.jpg");
    private final DirectoryReader directoryReader;
    //Tracks the last key press to ensure key press isn't registered when key is held
    private KeyCode eventTracker = null;
    //Used to store screenBounds dimensions as a variable to automatically resize imageView components to suit monitor
    private final Rectangle2D screenBounds;
    //Used for application logic and data handling to make this method a bit cleaner
    private final PhotoViewerApplicationLogic applicationLogic;
    //Class used to handle the logic of photo resizing, cropping, etc
    private final ConversionLogic conversionLogicClass;
    //Class used for handling metadata
    private final MetadataWrangler metadataWrangler;
    //Class used for user preferences
    private final UserPreferences userPreferences;
    //Class used for opening directory
    private final FileHandling fileHandling;
    //Label to display the metadata
    @FXML
    private Label metadataLabel;
    private PhotoConversionController photoConversionController;
    private HostServices hostServices;

    //Used for keeping track of the amount of level of scrolling for zoom calculations
    private double scrollAmountTracker = 0.0;

    public PhotoViewerController() {

        root = new StackPane();

        String directory =  "/home/hugh/Documents/Development/javaMemView/1.png"; //"/Users/hugh/Desktop/memview/Back Garden From stairs.png"; //"/home/hugh/Documents/Development/javaMemView/1.png";  // //"D:\\javaMemView\\1.jpg"; // /Users/hugh/Desktop/memview/Back Garden From stairs.png
        System.out.println("Directory: " + directory);

        //Instantiation of all the helper classes needed, Order is important
        directoryReader = new DirectoryReader(directory);
        conversionLogicClass = new ConversionLogic(directoryReader);
        userPreferences = new UserPreferences();
        applicationLogic = new PhotoViewerApplicationLogic(directoryReader, this);
        metadataWrangler = new MetadataWrangler(userPreferences, applicationLogic);
        fileHandling = new FileHandling(directoryReader);

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

    public void setServicesForPhotoController() {
        photoConversionController.setHelperObjectClasses(directoryReader, conversionLogicClass, fileHandling);
    }

    @FXML
    public void showConversionPopup() {
        photoConversionController.showPopup();
    }

    public void setPhotoConversionPopupController(PhotoConversionController photoConversionController) {
        this.photoConversionController = photoConversionController;
    }

    @FXML
    private void initialize() {

        //Creating a bufferedImage first as the Twelve Monkeys library creates buffered images unless a GIF then use native Image object
        File firstImagePath = directoryReader.getCurrentImagePath().toFile();
        Image firstImage = directoryReader.loadImage();

        mainImageView.setImage(firstImage);
        metadataLabel.setAlignment(Pos.TOP_LEFT);
        metadataLabel.toFront();
        getImageMetadata(firstImagePath.toPath());

        resizeImageForScreen(mainImageView);

        galleryThumbnailParentToolbar.setOpacity(0.0);
        galleryThumbnailParentToolbar.toFront();
        galleryThumbnailParentToolbar.setPrefHeight(screenBounds.getHeight() / 5);
        galleryThumbnailParentToolbar.setPrefWidth(root.getPrefWidth());


        applicationLogic.addPhotoThumbnailsToHBox(thumbnailContainerRibbon);

        //zoomBox initialization
        zoomBoxContainer = applicationLogic.initializeZoomBox(zoomBoxContainer);
        zoomBoxContainer.setOpacity(0.0);

        // Top file view ribbon initialization
        thumbnailContainerRibbon.setSpacing(75);
        thumbnailContainerRibbon.setPrefWidth(root.getPrefWidth());

        scrollPaneRootFileRibbon.setFitToHeight(true);
        scrollPaneRootFileRibbon.setMinViewportHeight(180);
        scrollPaneRootFileRibbon.setPrefWidth(root.getPrefWidth());

        mainImageView.fitWidthProperty().bind((root.widthProperty()));
    }

    @FXML
    public void nextButtonAction() {
        ImageAndPathHolder imageAndPathHolder = directoryReader.getNextImage();
        Path nextImageFilePath = imageAndPathHolder.getPath();
        Image nextImage = imageAndPathHolder.getImage();
        commonNextAndBackButtonAction(nextImageFilePath, nextImage);
    }

    private void commonNextAndBackButtonAction(Path path, Image image) {
        if(path.equals(outOfBoundsPath)) {
            mainImageView.setImage(new Image(outOfBoundsPath.toUri().toString()));
            return;
        }
        getImageMetadata(path);
        resetZoom();
        mainImageView.setImage(image);
    }

    @FXML
    public void backButtonAction() {
        ImageAndPathHolder imageAndPathHolder = directoryReader.getPreviousImage();
        Path nextImageFilePath = imageAndPathHolder.getPath();
        Image nextImage = imageAndPathHolder.getImage();
        commonNextAndBackButtonAction(nextImageFilePath, nextImage);
    }

    public void resizeImageForScreen(ImageView imageView) {
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(screenBounds.getHeight() - 100);
    }

    public void setTransparentToolbar() {
        galleryThumbnailParentToolbar.setOpacity(0.0);
    }

    public void setVisibleToolbar() {
        galleryThumbnailParentToolbar.setOpacity(100);
        galleryThumbnailParentToolbar.toFront();
    }

    @FXML
    public void keyPressHandler(KeyEvent event) {
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

    public void getImageMetadata(Path imageFile) {
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
    }

    @FXML
    private void scrollHandler(ScrollEvent scrollEvent) {
        //scrollAmountTracker used to keep track of different stages of zoom.

        boolean controlKeyPressed = eventTracker == KeyCode.CONTROL;
        double scrollEventAmount = scrollEvent.getDeltaY();

        if (controlKeyPressed && scrollEventAmount > 0 && scrollAmountTracker > 39) {
            zoomInActionSecondLevel();
            scrollAmountTracker += scrollEventAmount;

        } else if (controlKeyPressed && scrollEventAmount < 0 && scrollAmountTracker < -41) {
            zoomOutActionSecondLevel();
            scrollAmountTracker += scrollEvent.getDeltaY();

        } else if (controlKeyPressed && scrollEventAmount < 0 && scrollAmountTracker > -40) {
            scrollAmountTracker += scrollEvent.getDeltaY();
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

    private void resetZoom() {
        zoomOutActionFirstLevel();
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
        System.out.println("createZoomBoxOnClick method called+");
        zoomBoxContainer.toFront();
        zoomBoxContainer.setOpacity(100);
        zoomBoxContainer = applicationLogic.setZoomedImage(mainImageView, event);
        //Manipulation of zoomBoxContainer with the objective to sit on top of the mainImageView and show the zoomed section underneath
        //todo implement the above functionality
    }

    @FXML
    private void moveZoomBoxWithMouse(MouseEvent event) {
        if(zoomBoxContainer.getOpacity() == 100) {
            applicationLogic.moveOnMouseDragged(event, mainImageView);
        }
    }

    @FXML
    private void hideZoomBoxOnRelease() {
        System.out.println("Hide zoombox method called");
        applicationLogic.hideZoomBox();
        zoomBoxContainer.setOpacity(0.0);
        zoomBoxContainer.toBack();
    }


    public void gotoImageOnClick(Image imageToGoTo) {
        mainImageView.setImage(imageToGoTo);
    }

    public void updateMetadataLabel(Path imagePath) {
        getImageMetadata(imagePath);
    }



    @FXML
    private void keyReleasedHandler() {
        eventTracker = null;
    }

    @FXML
    private void toggleGPSExifPreferences() {
        if(GPSCheckMenu.isSelected()) {
            userPreferences.setMetadataGPSLabel(true);
            getImageMetadata(directoryReader.getCurrentImagePath());
        } else {
            userPreferences.setMetadataGPSLabel(false);
            getImageMetadata(directoryReader.getCurrentImagePath());
        }
    }

    @FXML
    private void toggleFileSizePreferences(){
        if(fileSizeCheckMenu.isSelected()) {
            userPreferences.setMetadataFileSizeLabel(true);
            getImageMetadata(directoryReader.getCurrentImagePath());
        } else {
            userPreferences.setMetadataFileSizeLabel(false);
            getImageMetadata(directoryReader.getCurrentImagePath());
        }
    }

    @FXML
    private void toggleCreationDatePreferences() {
        if(creationDateCheckMenu.isSelected()) {
            userPreferences.setMetadataCreationLabel(true);
            getImageMetadata(directoryReader.getCurrentImagePath());
        } else {
            userPreferences.setMetadataCreationLabel(false);
            getImageMetadata(directoryReader.getCurrentImagePath());
        }
    }
}