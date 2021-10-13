package directory.handling;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DirectoryReader {

    private File originalFilePath;
    private String originalFileName;
    private List<Path> fileNames;
    private int currentFileIndex;

    // Creates the DirectoryReader object to index all the files in the directory of the open file
    // THe originalFile object is the absolute Path of the file opened
    //todo change the constructor to a File object or whatever is appropriate for when a file is opened
    //todo Trim the whitespace out of a file name to prevent errors (needs to be done for File Path reasons)
    public DirectoryReader(String fileName) {

        originalFilePath = new File(fileName);
        originalFileName = fileName;
        fileNames = new ArrayList<>();

        //Reads the originalFilePath object and streams the list of
        // files in the directory to the fileNames List
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(originalFilePath.toPath().getParent())) {
            //For each loop to loop through all photos and add them to the fileNames list
            for (Path photos : directoryStream) {
                //Checks to see if there is a recursive directory, if so do not add
                if(!Files.isDirectory(photos)) {
                    fileNames.add(photos);
                    System.out.println(photos.getFileName().toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(fileNames.toString());

        // Finding the index of the first photo
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).equals(originalFilePath.toPath())) {
                currentFileIndex = i;
            }
        }

        System.out.println(currentFileIndex);
    }

    public Path getPreviousImage() {

        Path path = Paths.get("src/main/resources/image.Resources/testOutOfBoundsImage.png");

        if(fileNames.size() == 1) {
            return path;
        } else if (-1 == currentFileIndex - 1) {
            return path;

        } else {
            Path toRtn = fileNames.get(currentFileIndex - 1);
            currentFileIndex--;

            return toRtn;
        }

    }

    public Path getNextImage() {
        Path path = Paths.get("src/main/resources/image.Resources/testOutOfBoundsImage.png");

        if(fileNames.size() == 1) {
            return path;

        } else if (fileNames.size() == currentFileIndex + 1) {
            return path;

        } else {
            Path toRtn = fileNames.get(currentFileIndex + 1);
            currentFileIndex++;
            return toRtn;
        }
    }

    public int getCurrentFileIndex() {
        return currentFileIndex;
    }

    public File getPath() {return originalFilePath;}
    public List<Path> getPathList() { return fileNames;}

}