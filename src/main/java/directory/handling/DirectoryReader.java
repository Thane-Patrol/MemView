package directory.handling;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DirectoryReader {

    private final List<Path> fileNames;
    private int currentFileIndex;
    private final List<String> readableExtensionList;
    private final List<String> writableFileExtensionList;
    private final Path outOfBoundsImagePath;

    // Creates the DirectoryReader object to index all the files in the directory of the open file
    // THe originalFile object is the absolute Path of the file opened
    public DirectoryReader(String unsanitizedFileName) {
        outOfBoundsImagePath = Paths.get("src/main/resources/image.Resources/41nrqdLzutL._AC_SY580_.jpg");

        String sanitisedFileName = unsanitizedFileName.replaceAll("//s","");

        File originalFilePath = new File(sanitisedFileName);
        fileNames = new ArrayList<>();
        //Readable File Extensions
        readableExtensionList = FXCollections.observableArrayList();
        addReadableExtensionList();
        //Writable File Extensions
        writableFileExtensionList = FXCollections.observableArrayList();
        addWriteableFileExtensionsToList();

        addPhotosToList(originalFilePath);
        getFirstFileIndex(originalFilePath);
        fileNames.toString();
    }

    private void addPhotosToList(File originalFilePath) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(originalFilePath.toPath().getParent())) {
            //For each loop to loop through all photos and add them to the fileNames list
            for (Path photos : directoryStream) {
                //makes the extension lowercase
                String lowerCaseExtension = FilenameUtils.getExtension(photos.toString()).toLowerCase(Locale.ROOT);

                String fileNameAsString = FilenameUtils.removeExtension(photos.toString());
                String amendedPhoto = fileNameAsString + "." + lowerCaseExtension;
                Path amendedPhotoPath = Paths.get(amendedPhoto);
                //Checks to see if there is a recursive directory, if so do not add
                if(!Files.isDirectory(photos) && fileIsAPhoto(amendedPhotoPath)) {
                    fileNames.add(amendedPhotoPath);
                    System.out.println(amendedPhotoPath);
                }
                //todo figure out how to add photos to the fileName list while ignoring their case
            }
        } catch (Exception e) {
            System.out.println("Error Message:");
            System.out.println(e.getMessage());
        }
    }

    private void getFirstFileIndex(File originalFilePath) {
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).equals(originalFilePath.toPath())) {
                currentFileIndex = i;
            }
        }
    }

    private void addReadableExtensionList() {
        readableExtensionList.add(0, ".png");
        readableExtensionList.add(1, ".jpg");
        readableExtensionList.add(2, ".bmp");
        readableExtensionList.add(3, ".cur");
        readableExtensionList.add(4, ".ico");
        readableExtensionList.add(5, ".pict");
        readableExtensionList.add(6, ".pntg");
        readableExtensionList.add(7, ".pam");
        readableExtensionList.add(8, ".pbm");
        readableExtensionList.add(9, ".pgm");
        readableExtensionList.add(10, ".ppm");
        readableExtensionList.add(11, ".pfm");
        readableExtensionList.add(12, ".psd");
        readableExtensionList.add(12, ".psb");
        readableExtensionList.add(13, ".tga");
        readableExtensionList.add(15, ".webp");
        readableExtensionList.add(16, ".hdr");
        readableExtensionList.add(17, ".gif");
        readableExtensionList.add(18, ".tiff");
        readableExtensionList.add(19, ".tif");
        readableExtensionList.add(20, ".pcx");
        readableExtensionList.add(21, ".dcx");
        readableExtensionList.add(22, ".sgi");
    }

    private void addWriteableFileExtensionsToList() {
        writableFileExtensionList.add(0, ".bmp");
        writableFileExtensionList.add(1, ".ico");
        writableFileExtensionList.add(2, ".icns");
        writableFileExtensionList.add(3, ".jpg");
        writableFileExtensionList.add(4, ".pict");
        writableFileExtensionList.add(5, ".pnm");
        writableFileExtensionList.add(6, ".psd");
        writableFileExtensionList.add(7, ".tga");
        writableFileExtensionList.add(8, ".tiff");

    }

    public Path getCurrentImage() {
        return fileNames.get(currentFileIndex);
    }

    public Path getPreviousImage() {

        if(fileNames.size() == 1) {
            return outOfBoundsImagePath;
        } else if (-1 == currentFileIndex - 1) {
            return outOfBoundsImagePath;

        } else if (fileNames.size() == 0){
            return outOfBoundsImagePath;

        } else {
            Path toRtn = fileNames.get(currentFileIndex - 1);
            currentFileIndex--;
            return toRtn;
        }

    }

    public Path getNextImage() {

        if(fileNames.size() == 1) {
            return outOfBoundsImagePath;

        } else if (fileNames.size() == currentFileIndex + 1) {
            return outOfBoundsImagePath;

        } else if (fileNames.size() == 0) {
            return outOfBoundsImagePath;

        } else {
            Path toRtn = fileNames.get(currentFileIndex + 1);
            currentFileIndex++;
            return toRtn;
        }
    }

    public void setCurrentImageIndex(Path imagePath) {
        for(int i = 0; i < fileNames.size(); i++) {

            if (imagePath.equals(fileNames.get(i))) {
                currentFileIndex = i;
            }
        }
    }

    private boolean fileIsAPhoto(Path photoPath) {
        String extension = FilenameUtils.getExtension(photoPath.toString());
        for(String fileExtensionName : readableExtensionList) {
            if (fileExtensionName.contains(extension)) {
                return true;
            }
        }
        return false;
    }

    public String getPhotoExtension() {
        return FilenameUtils.getExtension(getCurrentImage().toString());
    }

    public Image loadImage() {
        Image image;
        try {
            File firstImagePath = this.getCurrentImage().toFile();

            if(this.getPhotoExtension().equals("gif")) {
                image = new Image(firstImagePath.toURI().toString());
            } else {
                image = SwingFXUtils.toFXImage(ImageIO.read(firstImagePath), null);
            }

            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image loadImageFromPath(Path imagePath) {
        Image image;
        try {
            File imageFile = imagePath.toFile();

            if(this.getPhotoExtension().equals("gif")) {
                image = new Image(imageFile.toURI().toString());
            } else {
                image = SwingFXUtils.toFXImage(ImageIO.read(imageFile), null);
            }

            return image;
        }  catch(IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    public List<Path> getListOfFilePaths() {
        return fileNames;
    }

    public String getDirectoryAsString() {
        return getCurrentImage().getParent().toString();
    }

    public List<String> getWritableFileExtensionList() {
        return writableFileExtensionList;
    }
}