package photo.conversion;

import directory.handling.DirectoryReader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConversionLogic {

    private final DirectoryReader directoryReader;

    public ConversionLogic(DirectoryReader directoryReader) {
        this.directoryReader = directoryReader;
    }

    public void convertPhotos(List<Path> filesToConvert, ParameterHolderHelper holderHelper) {
        String extensionCleaned = stripPeriodOffFileExtension(holderHelper.getExtensionToSaveAs());
        for(Path path : filesToConvert) {
            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
                bufferedImage = null;
            }

            ThumbnailLogicSwitcher thumbnailLogicSwitcher = new ThumbnailLogicSwitcher(bufferedImage, holderHelper);
            thumbnailLogicSwitcher.addResizePixels(holderHelper.isToResizeViaPixels());
            thumbnailLogicSwitcher.addRotate(holderHelper.isToRotate());
            thumbnailLogicSwitcher.addScalePixels(holderHelper.isToScale());
            thumbnailLogicSwitcher.addWatermark(holderHelper.isToWatermark());

            BufferedImage finalImage = thumbnailLogicSwitcher.getFinalImage();
            String fileNameSanitized = FilenameUtils.removeExtension(String.valueOf(path.getFileName()));

            File toSave = new File(holderHelper.getOutputPath() + fileNameSanitized + holderHelper.getExtensionToSaveAs());
            System.out.println("Filename to save: " + toSave.getAbsolutePath());

            try {
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
            if(radioButton.isSelected()) {
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
        if(radioButton.isSelected()) {
            return true;
        } else if(!directoryLabel.getText().isEmpty()) {
            return true;
        } else return !directoryLabel.getText().contains("Chosen Directory");
    }

    //returns true if invalid input is found in rotation textfield
    public boolean doesContainInvalidInputForRotation(String input) {
        String regex1 = "[^0-9]+";
        if(input.contains(regex1)) {
            return true;
        }
        int unsanitizedValue = Integer.parseInt(input);

        return unsanitizedValue > 360;
    }

    private String stripPeriodOffFileExtension(String foo) {
        return foo.replace(".", "");
    }

    //returns true with successful conversion
    public boolean checkForSuccessfulConversion(Path directoryPath, List<Path> pathListResized) {
        List<File> fileList = new ArrayList<>();

        final File directory = directoryPath.toFile();
        fileList.addAll(Arrays.asList(directory.listFiles()));

        return fileList.size() == pathListResized.size();
    }

}
