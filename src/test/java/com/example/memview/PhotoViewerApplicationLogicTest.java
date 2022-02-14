package com.example.memview;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import directory.handling.DirectoryReader;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
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

    }

    @Test
    public void testGetPhotoSizeInUnits() {
        Path imagePath = Paths.get("src/test/test.resources/images/IMG_20201230_130710_0006.jpg");
        BasicFileAttributes fileAttributes;
        try {
            fileAttributes = Files.readAttributes(imagePath, BasicFileAttributes.class);
        } catch(IOException e) {
            e.printStackTrace();
            fileAttributes = null;
        }
        String fileSizeWithBytes;

        long fileSize = fileAttributes.size();
        int KBCondition = 1000;
        int MBCondition = 1000_000;
        int GBCondition = 1000_000_000;
        //Used to check which branch was taken for testing
        int whichPath = 0;


        if (fileSize >= KBCondition && fileSize < MBCondition) {
            fileSizeWithBytes = fileSize/1000 + " KB";
            whichPath = 1;
        } else if (fileSize >= MBCondition && fileSize < GBCondition) {
            fileSizeWithBytes = fileSize/1000_000 + " MB";
            whichPath = 2;
        } else if (fileSize >= GBCondition) {
            fileSizeWithBytes = fileSize/1000_000_000 + " GB";
            whichPath = 3;
        } else {
            fileSizeWithBytes = fileSize + " bytes";
            whichPath = 4;
        }

        boolean toAssertValidBranchingChosen = 0 < whichPath && whichPath < 5;
        boolean toAssertCorrectExtension;
        if(fileSizeWithBytes.contains("KB") && whichPath == 1) {
            toAssertCorrectExtension = true;
        } else if(fileSizeWithBytes.contains("MB") && whichPath == 2) {
            toAssertCorrectExtension = true;
        } else if(fileSizeWithBytes.contains("GB") && whichPath == 3) {
            toAssertCorrectExtension = true;
        } else if(fileSizeWithBytes.contains("bytes") && whichPath == 4) {
            toAssertCorrectExtension = true;
        } else {
            toAssertCorrectExtension = false;
        }

        Assertions.assertTrue(toAssertValidBranchingChosen && toAssertCorrectExtension);

    }

    @Test
    public void testAddPhotoThumbnailsToHBox() {
        //Needed to prevent a toolkit exception. This is due to testing for FX components needing to be run on the FX thread
        JFXPanel jfxPanel = new JFXPanel();
        //labelList used as a check to verify all paths are added
        List<Label> labelList = new ArrayList<>();
        HBox hBox = new HBox();
        List<Path> filePaths = directoryReader.getListOfFilePaths();

        filePaths.stream().forEach(s -> {

            Label label = new Label(s.getFileName().toString());
            label.setText(s.getFileName().toString());

            ImageView imageView = new ImageView(directoryReader.loadImageFromPath(s));
            imageView.setFitHeight(200);

            VBox box = new VBox(imageView, label);
            labelList.add(label);

            hBox.getChildren().add(box);
        });

        boolean areAllImagesAdded;

        areAllImagesAdded = labelList.size() == filePaths.size();

        Assertions.assertTrue(areAllImagesAdded);
    }

    @Test
    public void testGetGPSCoordinates() {
        File imageFile = Paths.get("src/test/test.resources/images/IMG_20201230_130710_0006.jpg").toFile();
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

        Assertions.assertFalse(geoLocation == null);
    }


}
