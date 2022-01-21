package directory.handling;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;

public class DirectoryReader {

    private final List<Path> fileNames;
    private int currentFileIndex;
    private final Path outOfBoundsImagePath;
    private final List<String> fileExtensionList;

    //todo add a method to read movie files and be able to play back

    // Creates the DirectoryReader object to index all the files in the directory of the open file
    // THe originalFile object is the absolute Path of the file opened
    //todo change the constructor to a File object or whatever is appropriate for when a file is opened
    public DirectoryReader(String unsanitizedFileName) {

        String sanitisedFileName = unsanitizedFileName.replaceAll("//s","");

        File originalFilePath = new File(sanitisedFileName);
        fileNames = new ArrayList<>();
        outOfBoundsImagePath = Paths.get("src/main/resources/testOutOfBoundsImage.png");

        fileExtensionList = new ArrayList<>(7);
        addFileExtensionsToList();

        //String representation of potential end of file paths, used for checking if the filetype is a photo or not

        //Reads the originalFilePath object and streams the list of
        // files in the directory to the fileNames List
        //Does this to get an index of the images in a certain directory
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(originalFilePath.toPath().getParent())) {
            //For each loop to loop through all photos and add them to the fileNames list
            for (Path photos : directoryStream) {
                //Checks to see if there is a recursive directory, if so do not add
                if(!Files.isDirectory(photos) && fileIsAPhoto(photos)) {
                    fileNames.add(photos);
                }
            }
        } catch (Exception e) {
            System.out.println("Error Message:");
            System.out.println(e.getMessage());
        }
        System.out.println(fileNames);

        // Finding the index of the first photo
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).equals(originalFilePath.toPath())) {
                currentFileIndex = i;
            }
        }
    }

    private void addFileExtensionsToList() {
        fileExtensionList.add(0, ".png");
        fileExtensionList.add(1, ".jpg");
        fileExtensionList.add(2, ".bmp");
        fileExtensionList.add(3, ".webp");
        fileExtensionList.add(4, ".hdr");
        fileExtensionList.add(5, ".gif");
        fileExtensionList.add(6, ".tiff");
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
        for(String fileExtensionName : fileExtensionList) {
            if (photoPath.getFileName().toString().contains(fileExtensionName)) {
                return true;
            }
        }
        return false;
    }

    public String getPhotoExtension() {
        String extension = FilenameUtils.getExtension(getCurrentImage().toString());
        return extension;
    }

    public Image loadImage() throws IOException {
        File firstImagePath = this.getCurrentImage().toFile();
        Image image;

        if(this.getPhotoExtension().equals("gif")) {
            image = new Image(firstImagePath.toURI().toString());
        } else {
            image = SwingFXUtils.toFXImage(ImageIO.read(firstImagePath), null);
        }

        return image;
    }

    public Image loadImageFromPath(Path imagePath) throws IOException {
        File imageFile = imagePath.toFile();
        Image image;

        if(this.getPhotoExtension().equals("gif")) {
            image = new Image(imageFile.toURI().toString());
        } else {
            image = SwingFXUtils.toFXImage(ImageIO.read(imageFile), null);
        }

        return image;
    }

    //For debugging purposes
    public void printAllFilesAsString() {
        fileNames.stream().forEach(s -> System.out.println("File: " + s));
    }

    public List<Path> getListOfFilePaths() {
        return fileNames;
    }

}