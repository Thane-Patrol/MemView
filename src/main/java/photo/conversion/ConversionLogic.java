package photo.conversion;

import directory.handling.DirectoryReader;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class ConversionLogic {

    private final DirectoryReader directoryReader;

    public ConversionLogic(DirectoryReader directoryReader) {
        this.directoryReader = directoryReader;
    }

    //The pathToSaveOutput is assumed to be given as the root directory. The filename is obtained from the List<Path> parameter
    //
    public List<File> convertListOfFilesToConvert(List<Path> listOfFilesToConvert, String extensionToSaveAs, String pathToSaveOutput,
                                                  boolean toResize, int finalHeight, int finalWidth) {

        List<File> listOfConvertedFiles = new ArrayList<>();

        for (Path path : listOfFilesToConvert) {
            try {
                //Read Image into bufferedImage object, this makes the image file agnostic due to TwelveMonkeys
                BufferedImage originalImage = ImageIO.read(path.toFile());
                BufferedImage finalImage = originalImage;

                //Make the decision on further file manipulation with if statements
                if(toResize) {
                    finalImage = Thumbnails.of(originalImage).size(finalWidth, finalHeight).asBufferedImage();
                }
                //Get filename without the extension included
                String fileNameSanitized = FilenameUtils.removeExtension(String.valueOf(path.getFileName()));

                //Save the image to File as the extension requested, to the directory requested by user
                ImageIO.write(finalImage, extensionToSaveAs, new File(pathToSaveOutput +  fileNameSanitized + "." + extensionToSaveAs));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listOfConvertedFiles;
    }

    public List<List> getListOfRawFilesInDirectory() {
        List<RadioButton> radioButtonList = new ArrayList<>();
        List<HBox> hBoxList = new ArrayList<>();
        List<List> arrayOfLists = new ArrayList<>();


        for(Path path : directoryReader.getListOfFilePaths()) {
            Image image = new Image(path.toUri().toString(), 50, 0, true, false);
            System.out.println(path.getFileName());
            ImageView thumbnail = new ImageView(image);

            RadioButton radioButton = new RadioButton();
            radioButton.setText(path.getFileName().toString());
            radioButtonList.add(radioButton);

            HBox hBox = new HBox();
            hBox.getChildren().addAll(thumbnail, radioButton);
            hBoxList.add(hBox);
        }
        arrayOfLists.add(0, radioButtonList);
        arrayOfLists.add(1, hBoxList);
        arrayOfLists.add(2, directoryReader.getListOfFilePaths());


        return arrayOfLists;
    }

}
