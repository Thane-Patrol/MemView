package directory.handling;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;

public class FileHandling {
    private static String OPERATING_SYSTEM;
    private static Stage photoConversionStage;
    private static DirectoryChooser directoryChooser;

    public FileHandling() {
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
    }

    public void createDirectoryChoosingWindow() {
        directoryChooser.setTitle("Select a directory");
        directoryChooser.showDialog(photoConversionStage);
    }

    //todo mention in the README that linux OS need to have xdg-utils installed
    public void openSystemSpecific(String filePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (OPERATING_SYSTEM.contains("win")) {
            processBuilder.command("explorer", filePath);
        } else if (OPERATING_SYSTEM.contains("mac")){
            processBuilder.command("open", filePath);
            //linux is the catch all OS, this should be changed to support more OS's
        } else if (OPERATING_SYSTEM.contains("linux")) {
            //processBuilder.command("sh", "-c", "xdg-open", filePath);
            processBuilder.command("xdg-open", filePath);
        }
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

    public String getOperatingSystem() {
        return OPERATING_SYSTEM;
    }

    public void getPhotoConversionStage(Stage stage) {
        photoConversionStage = stage;
    }

}
