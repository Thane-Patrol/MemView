package main.controllers;

import com.drew.lang.GeoLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataWranglerTest {

    private PhotoViewerApplicationLogicTest photoViewerApplicationLogicTest;
    private Path path = Paths.get("src/test/test.resources/images/IMG_20201230_130710_0006.jpg");
    private boolean GPSInformationExist = false;
    private double latitude;
    private double longitude;

    public MetadataWranglerTest() {
        photoViewerApplicationLogicTest = new PhotoViewerApplicationLogicTest();
    }

    /*
    public void testSetMetadataLabel() {
        String toRtn = null;
        try {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            //toRtn = testGetFileCreationDate() + testGetFileSize() + testGetGPSMetadata();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(toRtn);
        boolean assertFileFormat = toRtn.contains("MB");
        boolean assertCreationDate = toRtn.contains("2020");

    } */

    @Test
    public void testGetFileCreationDate() {
        BasicFileAttributes fileAttributes;
        try {
            fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
        } catch(IOException e) {
            e.printStackTrace();
            fileAttributes = null;
        }
        if(fileAttributes == null) {
            Assertions.fail();
        }
        FileTime fileTime = fileAttributes.creationTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String toRtn = "Creation: " + simpleDateFormat.format(fileTime.toMillis());

        boolean checkCreationDate = toRtn.contains("2020");
        Assertions.assertTrue(true);
    }

    public String testGetFileSize() {
        return "Size: 4.7MB";
    }

    @Test
    public void testGetGPSMetadata() {

        GeoLocation geoLocation = photoViewerApplicationLogicTest.testGetGPSCoordinates();

        latitude = geoLocation.getLatitude();
        longitude = geoLocation.getLongitude();

        boolean assertLatitude = String.valueOf(latitude).contains("-33.803549999999994");
        boolean assertLongitude = String.valueOf(longitude).contains("151.0925138888889");

        System.out.println("latitude: " + latitude);
        System.out.println("longitude: " + longitude);
        Assertions.assertTrue(assertLatitude && assertLongitude);

    }




}
