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
import java.util.List;


public class ConversionLogic {

    private final DirectoryReader directoryReader;

    public ConversionLogic(DirectoryReader directoryReader) {
        this.directoryReader = directoryReader;
    }

    //The pathToSaveOutput is assumed to be given as the root directory. The filename is obtained from the List<Path> parameter
    public void convertListOfFilesToConvert(List<Path> listOfFilesToConvert, String extensionToSaveAs, String pathToSaveOutput,
                                                  boolean toResizeViaNumber, int finalHeight, int finalWidth,
                                            boolean resizeViaScalingFactor, double scalingFactor,
                                            boolean toRotate, int rotationAmount,
                                            boolean toApplyWatermark, double watermarkScale, Positions watermarkPosition, File watermarkFile, float opaquenessFactor) {
        //this strips the . off the file format as ImageIO.write needs the extension without the dot
        String extensionCleaned = stripPeriodOffFileExtension(extensionToSaveAs);
        for (Path path : listOfFilesToConvert) {
            try {
                //Read Image into bufferedImage object, this makes the image file agnostic due to TwelveMonkeys
                BufferedImage originalImage = ImageIO.read(path.toFile());
                BufferedImage finalImage = originalImage;

                //Pass of the conditional statements and checking to a helper class
                ThumbnailParameterBuilderObject thumbnailParameterBuilderObject = new ThumbnailParameterBuilderObject(finalImage, finalWidth, finalHeight, toResizeViaNumber,
                        scalingFactor, resizeViaScalingFactor,
                        toRotate, rotationAmount,
                        toApplyWatermark, watermarkScale, watermarkPosition, watermarkFile, opaquenessFactor);

                finalImage = thumbnailParameterBuilderObject.createFinalImageToReturn(finalImage);

                //Make the decision on further file manipulation with if statements
                /*
               // if(toResize) {
                   // finalImage = Thumbnails.of(originalImage).size(finalWidth, finalHeight).asBufferedImage();
                //}

                //Rotate file
                if(toRotate) {
                    //todo make this dependent on size specified or original size, whatever is specified by the user
                    finalImage = Thumbnails.of(originalImage).size(160, 160).rotate(rotationAmount).asBufferedImage();
                    System.out.println("rotation occuring");
                }

                //Apply watermark
                if(toApplyWatermark) {
                    finalImage = Thumbnails.of(originalImage).scale(1.0).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(watermarkFile), 0.5f).asBufferedImage();
                    System.out.println("watermark applying");
                }

                 */


                //Get filename without the extension included
                String fileNameSanitized = FilenameUtils.removeExtension(String.valueOf(path.getFileName()));
                System.out.println("FileName Sanitized: " + fileNameSanitized);

                File toSave = new File(pathToSaveOutput +  fileNameSanitized  + extensionToSaveAs);
                System.out.println("Final output for something not resized: " + toSave);

                //Save the image to File as the extension requested, to the directory requested by user
                ImageIO.write(finalImage, extensionCleaned, toSave);

            } catch (IOException e) {
                System.out.println("Printing Error message");
                System.out.println("---------------------------------");
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
        int unsanitizedValue = Integer.parseInt(input);

        return unsanitizedValue > 360;
    }

    private String stripPeriodOffFileExtension(String foo) {
        return foo.replace(".", "");
    }

    //returns the resized image if the only selected option is resize
    private BufferedImage getResizedImage(BufferedImage originalBufferedImage, int finalHeight, int finalWidth, double scalingFactor) {
        BufferedImage toRtn = null;
        if(scalingFactor == 0) {
            try {
                toRtn = Thumbnails.of(originalBufferedImage).size(finalWidth, finalHeight).asBufferedImage();
            } catch (IOException e) {
                System.out.println("IOException in ConversionLogic.getResizedImage method");
                System.out.println("Printing stack trace");
                e.printStackTrace();
            }
        } else {
            try {
                toRtn = Thumbnails.of(originalBufferedImage).scale(scalingFactor).asBufferedImage();
            } catch (IOException e) {
                System.out.println("IOException in ConversionLogic.getResizedImage method");
                System.out.println("Printing stack trace");
                e.printStackTrace();
            }
        }
        return toRtn;
    }

    //returns the rotated Image if the only selected option is rotate
    private BufferedImage getRotatedImage(BufferedImage originalBufferedImage, double rotationAmount) {
        int height = originalBufferedImage.getHeight();
        int width = originalBufferedImage.getWidth();
        BufferedImage toRtn = null;
        try {
            toRtn = Thumbnails.of(originalBufferedImage).size(width, height).rotate(rotationAmount).asBufferedImage();
        } catch (IOException e) {
            System.out.println("IOException in ConversionLogic.getRotatedImage method");
            System.out.println("--------------------");
            e.printStackTrace();
        }
        return toRtn;
    }
    //returns the rotated AND resized image
    private BufferedImage getRotatedAndResizedImage(BufferedImage bufferedImage, double rotationAmount, int finalHeight, int finalWidth) {
        BufferedImage toRtn = null;
        try {
            toRtn = Thumbnails.of(bufferedImage).size(finalWidth, finalHeight).rotate(rotationAmount).asBufferedImage();
        } catch (IOException e) {
            System.out.println("IOException in ConversionLogic.getRotatedAndResizedImage method");
            System.out.println("---------------");
            e.printStackTrace();
        }
        return toRtn;
    }

}
