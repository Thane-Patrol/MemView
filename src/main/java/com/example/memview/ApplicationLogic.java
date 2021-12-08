package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.application.Application;
import javafx.scene.control.ToolBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ApplicationLogic {

    private DirectoryReader directoryReader;

    public ApplicationLogic(DirectoryReader directoryReader) {
        this.directoryReader = directoryReader;
    }

    public String getPhotoSizeInUnits(Path imagePath) throws IOException {
        BasicFileAttributes fileAttributes = Files.readAttributes(imagePath, BasicFileAttributes.class);

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

    public ToolBar addPhotoThumbnailsToToolbar(ToolBar toolBar) {

        directoryReader.printAllFilesAsString();

        return toolBar;
    }
}
