package photo.conversion;

import java.nio.file.Path;
import java.util.List;

public class ConversionLogicHelper {
    private boolean successfulFileConversion;
    private List<Path> listOfPathsNotConverted;

    public void setListOfPathsNotConverted(List<Path> listOfPathsNotConverted) {
        this.listOfPathsNotConverted = listOfPathsNotConverted;
    }

    public List<Path> getListOfPathsNotConverted() {
        return listOfPathsNotConverted;
    }

    public boolean isSuccessfulFileConversion() {
        return successfulFileConversion;
    }

    public void setSuccessfulFileConversion(boolean successfulFileConversion) {
        this.successfulFileConversion = successfulFileConversion;
    }
}
