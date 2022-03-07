package main.controllers;

import directory.handling.DirectoryReader;
import directory.handling.FileHandling;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FilenameUtils;
import photo.conversion.ConversionLogic;
import photo.conversion.ListHolder;
import photo.conversion.ParameterHolderHelper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoConversionController {
    private Stage stage;
    private Stage mainStage;
    private ConversionLogic conversionLogic;
    private FileHandling fileHandling;
    private List<RadioButton> radioButtonList;
    private List<Path> pathList = new ArrayList<>();
    private DirectoryReader directoryReader;
    private File watermarkFile;
    private HashMap<String, Positions> positionHashMap;
    private boolean hasListOfFilesForConversionBeenAdded = false;
    @FXML
    private VBox radioButtonFileSelectVBox;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField widthTextField;
    @FXML
    private Label resizeLabel;
    @FXML
    private Label chosenDirectoryLabel;
    @FXML
    private CheckBox toResizeCheckBox;
    @FXML
    private CheckBox toApplyWatermarkCheckBox;
    @FXML
    private RadioButton saveToCurrentDirectoryRadioButton;
    @FXML
    private ChoiceBox<String> outputFileFormatChoiceBox;
    @FXML
    private CheckBox toRotateCheckBox;
    @FXML
    private TextField rotationAmountTextField;
    @FXML
    private Button chooseWaterMarkButton;
    @FXML
    private ChoiceBox<String> watermarkPositionCheckBox;
    @FXML
    private Slider watermarkScaleSlider;
    @FXML
    private Label watermarkOpacityLabel;
    @FXML
    private Label scalingLabel;
    @FXML
    private Slider scalingFactorSlider;
    @FXML
    private Slider watermarkOpacitySlider;

    public PhotoConversionController() {
        this.watermarkOpacitySlider = new Slider();
    }

    public void setHelperObjectClasses(DirectoryReader directoryReader, ConversionLogic conversionLogic, FileHandling fileHandling) {
        this.directoryReader = directoryReader;
        this.conversionLogic = conversionLogic;
        this.fileHandling = fileHandling;
    }

    @FXML
    private void initialize() {
        setResizeNodesToggle();
        setRotateNodesToggle();
        setWatermarkNodesToggle();
    }

    private void addListOfFilesToUserList() {
        if(!hasListOfFilesForConversionBeenAdded) {
            ListHolder listHolder = conversionLogic.getListOfRawFilesInDirectory();

            pathList = listHolder.getFilePathList();
            radioButtonList = listHolder.getRadioButtonList();
            List<HBox> hBoxList = listHolder.getHBoxList();
            radioButtonFileSelectVBox.getChildren().addAll(hBoxList);
            hasListOfFilesForConversionBeenAdded = true;
        }
    }

    @FXML
    private void hidePopup() {
        stage.hide();
    }

    @FXML
    private void storeListOfImagesToConvert() {
        List<Path> pathListToConvert;
        //todo implement checks to ensure files will not be converted into file types that will fail

        //Object used to store all the parameters, rather than passing them around
        ParameterHolderHelper holderHelper = new ParameterHolderHelper();

        //Checks for no photos selected otherwise they are added to the converted list
        if(showNoPhotosSelectedAlert()) {
            return;
        } else {
            pathListToConvert = conversionLogic.addImagesToConvertToList(radioButtonList, pathList);
        }

        boolean toResize = toResizeCheckBox.isSelected();

        if(toResize) {
            setResizeCheck(holderHelper);
        }

        if(showInvalidFileExtensionSpecifiedAlert()) {
            return;
        } else {
            setFileFormatCheck(holderHelper);
        }

        if(checkForInvalidDirectoryChosen()) {
            return;
        }


        setFileOutputPath(holderHelper);

        //Debugging to get rid of
        String amendedFilePath = holderHelper.getOutputPath();
        System.out.println("File path to save as: " + amendedFilePath);

        //Checks for rotation and checks for valid input
        boolean toRotate = toRotateCheckBox.isSelected();
        if(toRotate) {
            setRotateCheck(holderHelper);
        }

        boolean toWatermark = toApplyWatermarkCheckBox.isSelected();
        if(toWatermark) {
            if(checkForValidWatermarkSelection()) {
                setWatermarkCheck(holderHelper);
            }
        }

        //Check for images of the same file type
        //Needs to be called just before the actual conversion to understand user intent
        if(checkForImagesOfTheSameType(pathListToConvert, holderHelper) && holderHelper.checkForAnythingTransformationExceptFiles()) {
            showAlertFileTypesSelected();
        }

        conversionLogic.convertPhotos(pathListToConvert, holderHelper);

        Path directoryPath = Paths.get(amendedFilePath);
        conversionLogic.checkForSuccessfulFileConversion(directoryPath, pathListToConvert);
        if(conversionLogic.getConversionLogicHelper().isSuccessfulFileConversion()) {
            System.out.println("Conversion successful, all files converted");
            sendSuccessfulConversionAlert();
        } else {
            System.out.println("Conversion not successful, not all files converted");
            sendUnsuccessfulConversionAlert(conversionLogic.getConversionLogicHelper().getListOfPathsNotConverted());
        }
    }

    private void setResizeCheck(ParameterHolderHelper holderHelper) {
        if(showIncorrectResolutionSpecifiedAlert()) {
            System.out.println("Incorrect Resolution specified Alert");
        } else if (!heightTextField.getText().equals("")){
            holderHelper.setFinalHeight(Integer.parseInt(heightTextField.getText()));
            holderHelper.setFinalWidth(Integer.parseInt(widthTextField.getText()));
            holderHelper.setToResizeViaPixels(true);
        } else if (heightTextField.getText().equals("")) {
            holderHelper.setScalingFactor(scalingFactorSlider.getValue());
            holderHelper.setToScale(true);
        }
    }

    private void setFileFormatCheck(ParameterHolderHelper holderHelper) {
        holderHelper.setExtensionToSaveAs(outputFileFormatChoiceBox.getSelectionModel().getSelectedItem());
    }

    private void setFileOutputPath(ParameterHolderHelper holderHelper) {
        if(saveToCurrentDirectoryRadioButton.isSelected()) {
            holderHelper.setOutputPath(fileHandling.getPathInCorrectFormat(directoryReader.getDirectoryAsString()));
            chosenDirectoryLabel.setText(holderHelper.getOutputPath());
        } else {
            holderHelper.setOutputPath(fileHandling.getPathInCorrectFormat(chosenDirectoryLabel.getText()));
            chosenDirectoryLabel.setText(holderHelper.getOutputPath());
        }
    }

    private void setRotateCheck(ParameterHolderHelper holderHelper) {
        if(conversionLogic.doesContainInvalidInputForRotation(rotationAmountTextField.getText())) {
            showInvalidRotationAmountEntered();
        } else  {
            holderHelper.setToRotate(true);
            holderHelper.setRotationFactor(Double.parseDouble(rotationAmountTextField.getText()));
        }
    }

    private void setWatermarkCheck(ParameterHolderHelper holderHelper) {
        //todo implement alert for incorrect/invalid input
        holderHelper.setToWatermark(true);
        holderHelper.setWatermarkScale((float) watermarkScaleSlider.getValue());
        holderHelper.setOpaquenessFactor((float) watermarkOpacitySlider.getValue());
        holderHelper.setWatermarkFile(watermarkFile);
        holderHelper.setWatermarkPosition(getPositionFromCheckBox());
    }

    private void initializePopup() {
        outputFileFormatChoiceBox.getItems().addAll(directoryReader.getWritableFileExtensionList());
        outputFileFormatChoiceBox.setValue(".jpg");
        addListOfFilesToUserList();

        createPositionHashMap();
        watermarkPositionCheckBox.getItems().addAll("Top Left", "Top Center", "Top Right", "Center Left", "Center", "Center Right", "Bottom Left", "Bottom Center", "Bottom Right");
        watermarkPositionCheckBox.setValue("Bottom Right");

        setWatermarkScaleSlider();
    }

    private void setWatermarkScaleSlider() {

        watermarkScaleSlider.setValue(0);
        watermarkOpacitySlider.setValue(0);
    }

    private void createPositionHashMap() {
        positionHashMap = new HashMap<>(9);
        positionHashMap.put("Top Left", Positions.TOP_LEFT);
        positionHashMap.put("Top Center", Positions.TOP_CENTER);
        positionHashMap.put("Top Right", Positions.TOP_RIGHT);
        positionHashMap.put("Center Left", Positions.CENTER_LEFT);
        positionHashMap.put("Center", Positions.CENTER);
        positionHashMap.put("Center Right", Positions.CENTER_RIGHT);
        positionHashMap.put("Bottom Left", Positions.BOTTOM_LEFT);
        positionHashMap.put("Bottom Center", Positions.BOTTOM_CENTER);
        positionHashMap.put("Bottom Right", Positions.BOTTOM_RIGHT);
    }

    private Positions getPositionFromCheckBox() {
        return positionHashMap.get(watermarkPositionCheckBox.getValue());
    }

    public void showPopup() {
        initializePopup();
        stage.show();
    }

    public void setPopupStage(Stage popup) {
        this.stage = popup;
    }

    //Chosen output file directory
    public void openFileDirectoryToSpecifyOutputPath() {
        //This needs to be called before calling DirectoryChooser
        fileHandling.getPhotoConversionStage(this.stage);
        //The below needs to be called before calling the DirectoryChooser

        String toSetLabel = fileHandling.createDirectoryChoosingWindowForOutput();
        chosenDirectoryLabel.setText(toSetLabel);
    }


    //Choose watermark directory
    @FXML
    private void setWatermarkFromUserSpecifiedImage() {
        String toSetWatermarkButton = fileHandling.createFileChoosingWindowForWatermark();
        chooseWaterMarkButton.setText(toSetWatermarkButton);
        if (toSetWatermarkButton.contains("No Watermark Chosen")) {
            System.out.println("No watermark Chosen");
        } else {
            chooseWaterMarkButton.setText(toSetWatermarkButton);
            watermarkFile = new File(toSetWatermarkButton);
        }
    }

    //Will return true if Alert is shown
    private boolean showNoPhotosSelectedAlert() {
        System.out.println("showNoPhotosSelectedAlert method called");
        System.out.println("radioButtonList size: " + radioButtonList.size());
        if(conversionLogic.checkForOneImageSelected(radioButtonList)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select photos to convert");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return true;
        }
        return false;
    }

    //Will return true if Alert is shown
    private boolean showIncorrectResolutionSpecifiedAlert() {
        if(conversionLogic.checkForCorrectInputInImageSize(heightTextField, widthTextField)) {
            return false;
        } else if(!(scalingFactorSlider.getValue() == 0)) {
            return false;

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Illegal characters found - Please note that only whole numbers are allowed for resolution");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return true;
        }
    }
    //Will return true if Alert is shown
     private boolean showInvalidFileExtensionSpecifiedAlert() {
        if(outputFileFormatChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No output file format selected. Please select a output format");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return true;
        }
        return false;
     }

     //returns true if Alert shows
     private void showInvalidRotationAmountEntered() {
         Alert alert = new Alert(Alert.AlertType.ERROR, "Illegal characters found - Please note that only whole numbers from 0 to 360 are allowed for rotation");
         alert.showAndWait().filter(response -> response == ButtonType.OK);
     }

     //return true is Alert shows
     private boolean checkForInvalidDirectoryChosen() {
         if(!conversionLogic.checkForValidDirectoryChosen(saveToCurrentDirectoryRadioButton, chosenDirectoryLabel)) {
             Alert alert = new Alert(Alert.AlertType.ERROR, "Please specify a valid directory");
             alert.showAndWait().filter(response -> response == ButtonType.OK);
             return true;
         }
         return false;
     }

     @FXML
     private void setResizeNodesToggle() {
        if(toResizeCheckBox.isSelected()) {
            heightTextField.setDisable(false);
            widthTextField.setDisable(false);
            scalingFactorSlider.setDisable(false);
            scalingLabel.setDisable(false);
            resizeLabel.setDisable(false);
        } else {
            heightTextField.setDisable(true);
            widthTextField.setDisable(true);
            scalingFactorSlider.setDisable(true);
            scalingLabel.setDisable(true);
            resizeLabel.setDisable(true);
        }
     }

     @FXML
     private void setRotateNodesToggle() {
         rotationAmountTextField.setDisable(!toRotateCheckBox.isSelected());
     }

     @FXML
     private void setWatermarkNodesToggle() {
        if(toApplyWatermarkCheckBox.isSelected()) {
            chooseWaterMarkButton.setDisable(false);
            watermarkOpacitySlider.setDisable(false);
            watermarkScaleSlider.setDisable(false);
            watermarkPositionCheckBox.setDisable(false);
            watermarkOpacityLabel.setDisable(false);
        } else {
            chooseWaterMarkButton.setDisable(true);
            watermarkOpacitySlider.setDisable(true);
            watermarkScaleSlider.setDisable(true);
            watermarkPositionCheckBox.setDisable(true);
            watermarkOpacityLabel.setDisable(true);
        }
     }

     //returns true if there are images that have the same extension as the final output path
     private boolean checkForImagesOfTheSameType(List<Path> pathList, ParameterHolderHelper holderHelper) {
        List<Path> listOfPathsInSameExtension = new ArrayList<>();
        String currentExtension;
        String requestedExtension = holderHelper.getExtensionToSaveAs();

        for(Path path : pathList) {
            currentExtension = FilenameUtils.getExtension(path.getFileName().toString());
            System.out.println("Current extension: " + currentExtension);
            System.out.println("Requested extension: " + requestedExtension);
            if(requestedExtension.contains(currentExtension)) {
                listOfPathsInSameExtension.add(path);
            }
        }
        return listOfPathsInSameExtension.size() > 0;
     }

     private void showAlertFileTypesSelected() {
         Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid file extension - You have selected files to convert that are already in this file format");
         alert.showAndWait().filter(response -> response == ButtonType.OK);
     }

     private boolean checkForAllValidGUISelections() {
        return true;
     }

     private void sendSuccessfulConversionAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All files successfully converted");
        alert.showAndWait().filter(response -> response == ButtonType.OK);
     }

     //todo retrieve list of unsuccessful conversions and add them to alert
     private void sendUnsuccessfulConversionAlert(List<Path> unconvertedFiles) {
        StringBuilder alertText = new StringBuilder("Not all files successfully converted: \n");
        for(Path path : unconvertedFiles) {
            alertText.append("- ");
            alertText.append(path.getFileName().toString());
            alertText.append("\n");
        }
        String finalAlertText = alertText.toString();

        Alert alert = new Alert(Alert.AlertType.ERROR, finalAlertText );
        alert.showAndWait().filter(response -> response == ButtonType.OK);
     }

     //returns true if invalid user input is given, else Alert
     private boolean checkForValidWatermarkSelection() {
        if(chooseWaterMarkButton.getText().contains("No Watermark Chosen") || chooseWaterMarkButton.getText().contains("Choose watermark")) {
            sendNoWatermarkChosenAlert();
            return false;
        }
       return true;
     }

     private void sendNoWatermarkChosenAlert() {
         Alert alert = new Alert(Alert.AlertType.ERROR, "No watermark file chosen");
         alert.showAndWait().filter(response -> response == ButtonType.OK);
     }




}
