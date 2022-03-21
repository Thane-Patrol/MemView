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

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import directory.handling.DirectoryReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;

public class PhotoViewerApplicationLogic {
    private Pane zoomBoxPane;
    private ImageView zoomedImage;
    private final DirectoryReader directoryReader;
    private final PhotoViewerController photoViewerController;


    public PhotoViewerApplicationLogic(DirectoryReader directoryReader, PhotoViewerController photoViewerController) {
        this.directoryReader = directoryReader;
        this.photoViewerController = photoViewerController;
    }

    public String getPhotoSizeInUnits(Path imagePath) {
        BasicFileAttributes fileAttributes;
        try {
            fileAttributes = Files.readAttributes(imagePath, BasicFileAttributes.class);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        String fileSizeWithBytes;
        //Variables used to improve code readability and ease of future expandability
        long fileSize = fileAttributes.size();
        int KBCondition = 1000;
        int MBCondition = 1000_000;
        int GBCondition = 1000_000_000;

        if (fileSize >= KBCondition && fileSize < MBCondition) {
            fileSizeWithBytes = fileSize/1000 + " KB";

        } else if (fileSize >= MBCondition && fileSize < GBCondition) {
            fileSizeWithBytes = fileSize/1000_000 + " MB";

        } else if (fileSize >= GBCondition) {
            fileSizeWithBytes = fileSize/1000_000_000 + " GB";

        } else {
            fileSizeWithBytes = fileSize + " bytes";
        }

        return fileSizeWithBytes;
    }

    public void addPhotoThumbnailsToHBox(HBox hBox) {
        List<Path> filePaths = directoryReader.getListOfFilePaths();

        //Stream through this path, Create the labels, thumbnails and Vboxs to contain them and add all of them to the hbox then return it
        filePaths.stream().forEach(s -> {

            Label fileName = new Label();
            fileName.setText(s.getFileName().toString());
            fileName.setMaxWidth(180);
            System.out.println("loading: " + s.getFileName().toString());

            BufferedImage finalImage;
            Image image;
            try {
                BufferedImage bufferedImage = ImageIO.read(s.toFile());
                finalImage = Thumbnails.of(bufferedImage).size(150, 150).keepAspectRatio(true).asBufferedImage();
                image = SwingFXUtils.toFXImage(finalImage, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(180);
            imageView.setFitHeight(150);
            VBox vBox = new VBox(imageView, fileName);
            vBox.setMaxHeight(Screen.getPrimary().getBounds().getHeight()/8);

            //Set the mainImage view to the thumbnail when clicked on
            vBox.setOnMouseClicked(event -> {
                directoryReader.setCurrentImageIndex(s);
                photoViewerController.gotoImageOnClick(directoryReader.loadImage());
                photoViewerController.updateMetadataLabel(directoryReader.getCurrentImagePath());
            });

            hBox.getChildren().add(vBox);
        });
    }

    private void setMaxZoomBoxSize() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        zoomBoxPane.setMaxHeight(bounds.getHeight() / 10);
        zoomBoxPane.setMaxWidth(bounds.getHeight() / 10);
    }

    private void scaleImage(MouseEvent event) {
        zoomedImage.setScaleX(4);
        zoomedImage.setScaleY(4);
        Rectangle2D viewport = new Rectangle2D(event.getSceneX(), event.getSceneY(), 85, 85);
        zoomedImage.setViewport(viewport);
    }

    private void centerImageViewOverCursor(ImageView imageView, MouseEvent event) {
        zoomedImage = new ImageView(imageView.getImage());
        zoomedImage.setPreserveRatio(true);
        zoomBoxPane.setMaxWidth(300);
        zoomBoxPane.setMaxHeight(300);
        zoomBoxPane.setLayoutX(event.getSceneX());
        zoomBoxPane.setLayoutY(event.getSceneY());
        event.consume();
    }

    public void moveOnMouseDragged(MouseEvent mouseEvent, ImageView imageView) {
        zoomBoxPane.setTranslateX(mouseEvent.getSceneX() - zoomBoxPane.getLayoutX() - 0.5*zoomBoxPane.getWidth());
        zoomBoxPane.setTranslateY(mouseEvent.getSceneY() - zoomBoxPane.getLayoutY() - 0.5*zoomBoxPane.getHeight());
        scaleImage(mouseEvent);
        mouseEvent.consume();
        System.out.println("Move on mousedragged method called");
    }

    public Pane setZoomedImage(ImageView mainImageView, MouseEvent mouseEvent) {
        //setZoomBoxToRegionUnderMouse(mouseEvent);
        //ImageView imageView = scaleImage(mainImageView);
        centerImageViewOverCursor(mainImageView, mouseEvent);
        System.out.println("Zoombox x, y coords: " + zoomBoxPane.getLayoutX() + " y: " + zoomBoxPane.getLayoutY());
        //ImageView imageView = getPixelsUnderneathZoomBox(mainImageView);
        zoomBoxPane.getChildren().add(zoomedImage);
        return zoomBoxPane;
    }

    public void hideZoomBox() {
        zoomBoxPane.setOpacity(0);
        zoomedImage.setImage(null);
    }


    public Pane initializeZoomBox(Pane pane) {
        this.zoomBoxPane = pane;
        setMaxZoomBoxSize();
        //zoomBoxPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        zoomedImage = new ImageView();

        return zoomBoxPane;
    }

    public GeoLocation getGPSCoordinates(Path imagePath) {
        File imageFile = imagePath.toFile();
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
        } catch (ImageProcessingException | IOException imageProcessingException) {
            imageProcessingException.fillInStackTrace();
        }

        GeoLocation geoLocation = null;
        if(metadata == null) {
            return geoLocation;
        }
        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);


        for(GpsDirectory gpsDirectory : gpsDirectories) {
            geoLocation = gpsDirectory.getGeoLocation();

        }

        return geoLocation;
    }

    public boolean checkGeolocationForNull(GeoLocation geoLocation) {
        return geoLocation == null || geoLocation.isZero();
    }
}
