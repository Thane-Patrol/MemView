package com.example.memview;

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
import photo.conversion.ConversionLogic;
import photo.conversion.ParameterHolderHelper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

public class PhotoConversionController {
    private Stage stage;
    private Stage mainStage;
    private PhotoViewerController photoViewerController;
    private ConversionLogic conversionLogic;
    private FileHandling fileHandling;
    private List<RadioButton> radioButtonList;
    private List<Path> pathList = new ArrayList<>();
    private DirectoryReader directoryReader;
    private File watermarkFile;
    private HashMap<String, Positions> positionHashMap;
    @FXML
    private VBox radioButtonFileSelectVBox;
    @FXML
    private CheckBox keepAspectRatioCheckBox;
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
    private ChoiceBox outputFileFormatChoiceBox;
    @FXML
    private CheckBox toRotateCheckBox;
    @FXML
    private TextField rotationAmountTextField;
    @FXML
    private Button chooseWaterMarkButton;
    @FXML
    private ChoiceBox watermarkPositionCheckBox;
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

    public void setHelperObjectClasses(DirectoryReader directoryReader, ConversionLogic conversionLogic, FileHandling fileHandling, PhotoViewerController photoViewerController) {
        this.directoryReader = directoryReader;
        this.conversionLogic = conversionLogic;
        this.fileHandling = fileHandling;
        this.photoViewerController = photoViewerController;
    }

    @FXML
    private void initialize() {
        setResizeNodesToggle();
        setRotateNodesToggle();
        setWatermarkNodesToggle();
    }

    private void addListOfFilesToUserList() {
        List<List> arrayOfLists = conversionLogic.getListOfRawFilesInDirectory();

        pathList = arrayOfLists.get(2);
        radioButtonList = arrayOfLists.get(0);
        List<HBox> hBoxList = arrayOfLists.get(1);
        radioButtonFileSelectVBox.getChildren().addAll(hBoxList);
    }

    @FXML
    private void hidePopup() {
        stage.hide();
    }

    @FXML
    private void storeListOfImagesToConvert() {
        List<Path> pathListToConvert;
        //todo check if image selected is already of the file type being converted if so do not add it
        //todo create prompt to tell user that particular image/s are already of the selected file type
        //todo then have option to uncheck the images manually or automatically deselect all
        //todo have a method for checking file renaming
        //todo implement checks to ensure files will not be converted into file types that will fail

        //Object used to store all the parameters, rather than passing them around
        ParameterHolderHelper holderHelper = new ParameterHolderHelper();

        //Checks for no photos selected otherwise they are added to the converted list
        if(showNoPhotosSelectedAlert()) {
            return;
        } else {
            pathListToConvert = conversionLogic.addImagesToConvertToList(radioButtonList, pathList);
        }

        //todo grey out all the resizing options if toResize is not selected, also make it not necessary to specify resolution heights if it is selected
        boolean toResize = toResizeCheckBox.isSelected();

        if(toResize) {
            holderHelper = setResizeCheck(holderHelper);
        }

        if(showInvalidFileExtensionSpecifiedAlert()) {
            return;
        } else {
            holderHelper = setFileFormatCheck(holderHelper);
        }

        if(checkForInvalidDirectoryChosen()) {
            return;
        }


        holderHelper = setFileOutputPath(holderHelper);

        //Debugging to get rid of
        String amendedFilePath = holderHelper.getOutputPath();
        System.out.println("File path to save as: " + amendedFilePath);

        //Checks for rotation and checks for valid input
        boolean toRotate = toRotateCheckBox.isSelected();
        if(toRotate) {
            holderHelper = setRotateCheck(holderHelper);
        }

        //todo check if the watermarkChosen boolean actually changes when the user wants it to
        boolean toWatermark = toApplyWatermarkCheckBox.isSelected();
        if(toWatermark) {
            holderHelper = setWatermarkCheck(holderHelper);
        }

        conversionLogic.convertPhotos(pathListToConvert, holderHelper);
        /*
        if(succesesfulConversion) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Conversion successful!");
            alert.showAndWait().filter(response -> response == ButtonType.FINISH);
        }
        //todo add a popup to tell user that conversion is successful or not
        */

        Path directoryPath = Paths.get(amendedFilePath);
        directoryPath.toString();
        if(conversionLogic.checkForSuccessfulConversion(directoryPath, pathListToConvert)) {
            System.out.println("Conversion successful, all files converted");
        } else {
            System.out.println("Conversion not successful, not all files converted");
        }
    }

    private ParameterHolderHelper setResizeCheck(ParameterHolderHelper holderHelper) {
        if(showIncorrectResolutionSpecifiedAlert()) {
            return holderHelper;
        } else if (heightTextField.getText() != null){
            holderHelper.setFinalHeight(Integer.valueOf(heightTextField.getText()));
            holderHelper.setFinalWidth(Integer.valueOf(widthTextField.getText()));
            holderHelper.setToResizeViaPixels(true);
        } else if (heightTextField.getText() == null) {
            holderHelper.setScalingFactor(scalingFactorSlider.getValue());
            holderHelper.setToScale(true);
        }
        return holderHelper;
    }

    private ParameterHolderHelper setFileFormatCheck(ParameterHolderHelper holderHelper) {
        holderHelper.setExtensionToSaveAs(outputFileFormatChoiceBox.getSelectionModel().getSelectedItem().toString());
        return holderHelper;
    }

    private ParameterHolderHelper setFileOutputPath(ParameterHolderHelper holderHelper) {
        if(saveToCurrentDirectoryRadioButton.isSelected()) {
            holderHelper.setOutputPath(fileHandling.getPathInCorrectFormat(directoryReader.getDirectoryAsString()));
            chosenDirectoryLabel.setText(holderHelper.getOutputPath());
        } else {
            holderHelper.setOutputPath(fileHandling.getPathInCorrectFormat(chosenDirectoryLabel.getText()));
            chosenDirectoryLabel.setText(holderHelper.getOutputPath());
        }
        return holderHelper;
    }

    private ParameterHolderHelper setRotateCheck(ParameterHolderHelper holderHelper) {
        if(conversionLogic.doesContainInvalidInputForRotation(rotationAmountTextField.getText())) {
            showInvalidRotationAmountEntered();
            return holderHelper;
        } else  {
            holderHelper.setRotationFactor(Double.valueOf(rotationAmountTextField.getText()));
        }
        return holderHelper;
    }

    private ParameterHolderHelper setWatermarkCheck(ParameterHolderHelper holderHelper) {
        //todo implement alert for incorrect/invalid input
        holderHelper.setOpaquenessFactor((float) watermarkOpacitySlider.getValue());
        holderHelper.setWatermarkFile(watermarkFile);
        holderHelper.setWatermarkPosition(getPositionFromCheckBox());
        return holderHelper;
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
        return positionHashMap.get(watermarkPositionCheckBox.getValue().toString());
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
        if(!conversionLogic.checkForCorrectInputInImageSize(heightTextField, widthTextField)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Illegal characters found - Please note that only whole numbers are allowed for resolution");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return true;
        }
        return false;
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
     private boolean showInvalidRotationAmountEntered() {
        if(conversionLogic.doesContainInvalidInputForRotation(rotationAmountTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Illegal characters found - Please note that only whole numbers from 0 to 360 are allowed for rotation");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return true;
        }
        return false;
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
        if(toRotateCheckBox.isSelected()) {
            rotationAmountTextField.setDisable(false);
        } else {
            rotationAmountTextField.setDisable(true);
        }
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

     private boolean checkForAllValidGUISelections() {
        return true;
     }


}
