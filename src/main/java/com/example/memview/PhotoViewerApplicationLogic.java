package com.example.memview;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import directory.handling.DirectoryReader;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import com.drew.imaging.ImageMetadataReader;
import javafx.stage.Screen;

public class PhotoViewerApplicationLogic {

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

            ImageView imageView = new ImageView(directoryReader.loadImageFromPath(s));
            imageView.setPreserveRatio(true);
            //todo set height based upon reasonable screenbounds and/or current window size
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);
            VBox vBox = new VBox(imageView, fileName);
            vBox.setMaxHeight(Screen.getPrimary().getBounds().getHeight()/8);

            //Set the mainImage view to the thumbnail when clicked on
            vBox.setOnMouseClicked(event -> {
                directoryReader.setCurrentImageIndex(s);
                photoViewerController.gotoImageOnClick(directoryReader.loadImage());
                photoViewerController.updateMetadataLabel(directoryReader.getCurrentImage());
            });

            hBox.getChildren().add(vBox);
        });


    }

    //Used to get the region of the image underneath the main image
    //todo implement
    public Image getImageUnderneathZoomBoxContainer(Pane zoomBox, ImageView mainImageView, MouseEvent mouseEvent) {
        return null;
    }

    public boolean isZoomBoxOverTheMainImage(Pane zoomBox, ImageView mainImageView) {
        return zoomBox.intersects(mainImageView.getBoundsInLocal());
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
