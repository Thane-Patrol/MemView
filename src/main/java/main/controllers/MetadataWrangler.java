package main.controllers;

import com.drew.lang.GeoLocation;
import preferences.UserPreferences;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class MetadataWrangler {

    private final UserPreferences userPreferences;
    private final PhotoViewerApplicationLogic applicationLogic;
    private boolean GPSInformationExist = false;
    private double latitude;
    private double longitude;

    public MetadataWrangler(UserPreferences userPreferences, PhotoViewerApplicationLogic applicationLogic) {
        this.userPreferences = userPreferences;
        this.applicationLogic = applicationLogic;
    }

    //This method should be called for every image
    public String setMetadataLabel(Path filePath){
        try {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);
            return getFileCreationDate(basicFileAttributes) + getFileSize(filePath) + getGPSMetadata(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileCreationDate(BasicFileAttributes fileAttributes) {
        String toRtn = "";
        if(userPreferences.getMetadataCreationLabel()) {
            FileTime fileTime = fileAttributes.creationTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

            toRtn = "Creation: " + simpleDateFormat.format(fileTime.toMillis());
        }
        return toRtn;
    }

    private String getFileSize(Path filePath) {
        String toRtn = "";
        if(userPreferences.getMetadataFileSizeLabel()) {
            toRtn = " Size: " + applicationLogic.getPhotoSizeInUnits(filePath);
        }

        return toRtn;
    }

    private String getGPSMetadata(Path filePath) {
        String toRtn = "";
        if(userPreferences.getMetadataGPSLabel()) {
            GeoLocation geoLocation = applicationLogic.getGPSCoordinates(filePath);

            if (applicationLogic.checkGeolocationForNull(geoLocation)) {
                toRtn = " No GPS information found";
                GPSInformationExist = false;

            } else {
                toRtn = " GPS: " + geoLocation.toDMSString();
                GPSInformationExist = true;
                latitude = geoLocation.getLatitude();
                longitude = geoLocation.getLongitude();
            }
        }

        return toRtn;
    }

    //If true then GPS information exists
    public boolean getGPSMetadataForNullBoolean() {
        return GPSInformationExist;
    }

    public String getGoogleMapsURL() {
        return "https://maps.google.com/?q=" + latitude + "," + longitude;
    }



}