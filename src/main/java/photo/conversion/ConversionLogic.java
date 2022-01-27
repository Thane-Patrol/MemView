package photo.conversion;

import directory.handling.DirectoryReader;
import javafx.scene.control.*;
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
    public void convertListOfFilesToConvert(List<Path> listOfFilesToConvert, String extensionToSaveAs, String pathToSaveOutput,
                                                  boolean toResize, int finalHeight, int finalWidth, boolean toRotate, int rotationAmount) {
        //this strips the . off the file format as ImageIO.write needs the extension without the dot
        String extensionCleaned = stripPeriodOffFileExtension(extensionToSaveAs);
        for (Path path : listOfFilesToConvert) {
            try {
                //todo have a series of switch statements to cover every option of resizing combinations (resolution, rotation, watermark, etc)
                //Read Image into bufferedImage object, this makes the image file agnostic due to TwelveMonkeys
                BufferedImage originalImage = ImageIO.read(path.toFile());
                BufferedImage finalImage = originalImage;

                //Make the decision on further file manipulation with if statements
                if(toResize) {
                    finalImage = Thumbnails.of(originalImage).size(finalWidth, finalHeight).asBufferedImage();

                }

                //Rotate file
                if(toRotate) {
                    //todo make this dependent on size specified or original size, whatever is specified by the user
                    finalImage = Thumbnails.of(originalImage).size(160, 160).rotate(rotationAmount).asBufferedImage();
                    System.out.println("rotation occuring");
                }
                //Get filename without the extension included
                String fileNameSanitized = FilenameUtils.removeExtension(String.valueOf(path.getFileName()));
                System.out.println("FileName Sanitized: " + fileNameSanitized);

                File toSave = new File(pathToSaveOutput +  fileNameSanitized  + extensionToSaveAs);
                System.out.println("Final output for something not resized: " + toSave.toString());

                //Save the image to File as the extension requested, to the directory requested by user
                ImageIO.write(finalImage, extensionCleaned, toSave);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<List> getListOfRawFilesInDirectory() {
        List<RadioButton> radioButtonList = new ArrayList<>();
        List<HBox> hBoxList = new ArrayList<>();
        List<List> arrayOfLists = new ArrayList<>();


        for(Path path : directoryReader.getListOfFilePaths()) {
            Image image = new Image(path.toUri().toString(), 50, 0, true, false);
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

    //return true if valid input is found
    public boolean checkForCorrectInputInImageSize(TextField leftTextbox, TextField rightTextbox) {

        if(leftTextbox.getText().isBlank() || rightTextbox.getText().isBlank()) {
            return false;
        }

        //This regex checks for all input that contains anything except numbers, including characters or periods "."
        String regexForChecking = "[^0-9]+";
        return !leftTextbox.getText().contains(regexForChecking) && !rightTextbox.getText().contains(regexForChecking);
    }

    //return true if no images are selected
    public boolean checkForOneImageSelected(List<RadioButton> radioButtonList) {
        int j = 0;
        for (RadioButton radioButton : radioButtonList) {
            if(!radioButton.isSelected()) {
                j++;
            }
        }
        return j == 0;
    }

    public List<Path> addImagesToConvertToList(List<RadioButton> radioButtonList, List<Path> pathList) {
        List<Path> listOfSelectedFilePaths = new ArrayList<>();
        for(RadioButton radioButton : radioButtonList) {
            if(radioButton.isSelected()) {
                for(Path path : pathList) {
                    if(path.getFileName().toString().equals(radioButton.getText())) {
                        listOfSelectedFilePaths.add(path);
                    }
                }
            }
        }
        return listOfSelectedFilePaths;
    }

    //return true if valid directory specified by user
    public boolean checkForValidDirectoryChosen(RadioButton radioButton, Label directoryLabel) {
        return radioButton.isSelected() || !directoryLabel.getText().isEmpty() || !directoryLabel.getText().equals("Chosen Directory");

    }

    //returns true if invalid input is found in rotation textfield
    public boolean doesContainInvalidInputForRotation(String input) {
        String regex1 = "[^0-9]+";
        if(input.contains(regex1)) {
            return true;
        }
        int unsanitizedValue = Integer.valueOf(input);

        if (unsanitizedValue > 360) {
            return true;
        }
         return false;
    }

    public DirectoryReader getDirectoryReader() {
        return directoryReader;
    }

    private String stripPeriodOffFileExtension(String foo) {
        return foo.replace(".", "");
    }
}
