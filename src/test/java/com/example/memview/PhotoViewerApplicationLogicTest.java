package com.example.memview;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import directory.handling.DirectoryReader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhotoViewerApplicationLogicTest {
    private DirectoryReader directoryReader;
    private double vBoxHeight;
    private PhotoViewerController photoViewerController;

    public PhotoViewerApplicationLogicTest(){
        directoryReader = new DirectoryReader("src/test/test.resources/images/1.png");
        vBoxHeight = 250;
        photoViewerController = new PhotoViewerController();
    }

    @Test
    public void addPhotoThumbnailsToHBox() {
        HBox hBox = new HBox();
        List<Path> filePaths = directoryReader.getListOfFilePaths();

        filePaths.stream().forEach(s -> {

            Label label = new Label();
            label.setText(s.getFileName().toString());

            ImageView imageView = new ImageView(directoryReader.loadImageFromPath(s));
            imageView.setFitHeight(200);

            VBox box = new VBox(imageView, label);

            box.setOnMouseClicked(event -> {
                directoryReader.setCurrentImageIndex(s);
                photoViewerController.gotoImageOnClick(directoryReader.loadImage());
                photoViewerController.updateMetadataLabel(directoryReader.getCurrentImage());
            });
            hBox.getChildren().add(box);
        });
    }

    @Test
    public void testGetGPSCoordinates() {
        File imageFile = directoryReader.getCurrentImage().toFile();
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);

        GeoLocation geoLocation = null;
        for(GpsDirectory gpsDirectory : gpsDirectories) {
            geoLocation = gpsDirectory.getGeoLocation();
        }


    }


}
