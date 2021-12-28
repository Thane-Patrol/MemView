package photo.conversion;

import com.example.memview.HelloController;
import directory.handling.DirectoryReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConversionLogic {

    private DirectoryReader directoryReader;

    public ConversionLogic(DirectoryReader directoryReader) {
        this.directoryReader = directoryReader;
    }

    public List<File> convertListOfFilesToJPG(List<File> listOfFilesToConvert) {
        List<File> listOfConvertedFiles = new ArrayList<>();

        return listOfConvertedFiles;
    }

    public List<File> resizePhotosToSize(List<File> listOfFilesToResize,
                                         boolean keepAspectRatio, int finalPixelHeight, int finalPixelWidth) {
        List<File> listOfResizedFiles = new ArrayList<>();

        return listOfResizedFiles;
    }

}
