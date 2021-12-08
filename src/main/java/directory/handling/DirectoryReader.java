package directory.handling;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DirectoryReader {

    private final List<Path> fileNames;
    private int currentFileIndex;
    private final Path outOfBoundsImagePath;

    // Creates the DirectoryReader object to index all the files in the directory of the open file
    // THe originalFile object is the absolute Path of the file opened
    //todo change the constructor to a File object or whatever is appropriate for when a file is opened
    public DirectoryReader(String unsanitizedFileName) {

        String sanitisedFileName = unsanitizedFileName.replaceAll("//s","");

        File originalFilePath = new File(sanitisedFileName);
        fileNames = new ArrayList<>();
        outOfBoundsImagePath = Paths.get("src/main/resources/testOutOfBoundsImage.png");

        //Reads the originalFilePath object and streams the list of
        // files in the directory to the fileNames List
        //Does this to get an index of the images in a certain directory
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(originalFilePath.toPath().getParent())) {
            //For each loop to loop through all photos and add them to the fileNames list
            for (Path photos : directoryStream) {
                //Checks to see if there is a recursive directory, if so do not add
                if(!Files.isDirectory(photos)) {
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

    //For debugging purposes
    public void printAllFilesAsString() {
        fileNames.stream().forEach(s -> System.out.println("File: " + s));
    }

    public List<Path> getListOfFilePaths() {
        return fileNames;
    }

}