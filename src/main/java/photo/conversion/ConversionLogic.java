package photo.conversion;

import directory.handling.DirectoryReader;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.coobird.thumbnailator.Thumbnails;

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

    //Class used to determine
    public List<File> convertListOfFilesToConvert(List<Path> listOfFilesToConvert, String extension, String pathToSaveOutput,
                                                  boolean toResize, boolean keepAspectRatio, int finalHeight, int finalWidth) {

        List<File> listOfConvertedFiles = new ArrayList<>();

        if (toResize) {
            resizePhotosToSize(listOfFilesToConvert, keepAspectRatio, finalHeight, finalWidth);
        } else {
            for (Path path : listOfFilesToConvert) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(path.toFile());
                    ImageIO.write(bufferedImage, extension, new File(pathToSaveOutput + path.getFileName() + "." + extension));



                    //Directory File
                    File dir = new File(pathToSaveOutput);
                    //Output file to write to
                    File outputFile = new File(dir, path.getFileName() + "." + extension);
                    ImageIO.write(bufferedImage, extension, outputFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        return listOfConvertedFiles;
    }

    private void saveFilesToDisk(List<Path> listOfFilesToSave, String directory) {

    }

    public List<File> resizePhotosToSize(List<Path> listOfFilesToResize,
                                         boolean keepAspectRatio, int finalPixelHeight, int finalPixelWidth) {
        List<File> listOfResizedFiles = new ArrayList<>();
        BufferedImage finalImage;

        for (Path path : listOfFilesToResize) {
            try {
                BufferedImage originalImage = ImageIO.read(path.toFile());
                finalImage = Thumbnails.of(originalImage).size(finalPixelWidth, finalPixelHeight).asBufferedImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listOfResizedFiles;
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
