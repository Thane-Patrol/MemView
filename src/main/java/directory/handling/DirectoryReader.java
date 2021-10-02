package directory.handling;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirectoryReader {

    private File originalFilePath;
    private String originalFileName;
    private List<Path> fileNames;

    // Creates the DirectoryReader object to index all the files in the directory of the open file
    // THe originalFile object is the absolute Path of the file opened
    //todo change the constructor to a File object or whatever is appropriate for when a file is opened
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
    }

    public Path getNext() {
        Path toRtn = null;

        // Go through the fileNames array and find the index of the next image
        for(int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).getFileName().equals(originalFileName)) {
                toRtn = fileNames.get(i + 1).getFileName();
            }
        }

        return toRtn;
    }

    public String getPath() {return originalFileName;}
    public List<Path> getPathlist() { return fileNames;}

}