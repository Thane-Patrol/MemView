package com.example.memview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import preferences.UserPreferences;

import java.nio.file.Path;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataWranglerTest {
    private final UserPreferences userPreferences;
    private final PhotoViewerApplicationLogicTest applicationLogic;
    private boolean GPSInformationExist = false;
    private double latitude;
    private double longitude;

    public MetadataWranglerTest() {
        userPreferences = new UserPreferences();
        applicationLogic = new PhotoViewerApplicationLogicTest();
    }

    @Test
    public void testSetMetadataLabel() {
        Path path = Paths.get("src/test/test.resources/images/IMG_20201230_130710_0006.jpg");
    }



}
