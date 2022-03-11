package directory.handling;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileHandling {
    private static String OPERATING_SYSTEM;
    private static Stage photoConversionStage;
    private static DirectoryChooser directoryChooser;
    private static FileChooser fileChooser;

    public FileHandling(DirectoryReader directoryReader) {
        String s = System.getProperty("os.name").toLowerCase();

        if(s.contains("win")) {
            OPERATING_SYSTEM = "windows";
        }
        if(s.contains("mac")) {
            OPERATING_SYSTEM = "macos";
        }
        if (s.contains("linux")) {
            OPERATING_SYSTEM = "linux";
        }
        this.directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory");
        directoryChooser.setInitialDirectory(directoryReader.getCurrentImagePath().getParent().toFile().getAbsoluteFile());

        fileChooser = new FileChooser();
        fileChooser.setTitle("Select a watermark");
        fileChooser.setInitialDirectory(new File(directoryReader.getDirectoryAsString()));


    }

    public String createDirectoryChoosingWindowForOutput() {
        final File selectedDirectory = directoryChooser.showDialog(photoConversionStage);

        if(selectedDirectory != null) {
            selectedDirectory.getAbsolutePath();
        } else {
            return "No Directory Chosen";
        }
        return selectedDirectory.getAbsolutePath();
    }

    public String createFileChoosingWindowForWatermark() {
        final File selectedFile = fileChooser.showOpenDialog(photoConversionStage);

        if(selectedFile != null) {
            selectedFile.getAbsolutePath();
        } else {
            return "No Watermark Chosen";
        }
        System.out.println(selectedFile.getAbsolutePath());
        return selectedFile.getAbsolutePath();
    }

    public void getPhotoConversionStage(Stage stage) {
        photoConversionStage = stage;
    }

    public String getPathInCorrectFormat(String path) {
        String toRtn;
        if(OPERATING_SYSTEM.contains("win")) {
            toRtn = path + "\\";
        } else {
            toRtn = path + "/";
        }
        return toRtn;
    }



}
