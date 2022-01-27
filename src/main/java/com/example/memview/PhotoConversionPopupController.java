package com.example.memview;

import directory.handling.DirectoryReader;
import directory.handling.FileHandling;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photo.conversion.ConversionLogic;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PhotoConversionPopupController {
    private Stage stage;
    private Stage mainStage;
    private MainController mainController;
    private ConversionLogic conversionLogic;
    private FileHandling fileHandling;
    private List<RadioButton> radioButtonList;
    private List<Path> pathList;
    private DirectoryReader directoryReader;
    @FXML
    private VBox radioButtonFileSelectVBox;
    @FXML
    private CheckBox keepAspectRatioCheckBox;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField widthTextField;
    @FXML
    private Label chosenDirectoryLabel;
    @FXML
    private CheckBox toResizeCheckBox;
    @FXML
    private RadioButton saveToCurrentDirectoryRadioButton;
    @FXML
    private ChoiceBox outputFileFormatChoiceBox;
    @FXML
    private CheckBox toRotateCheckBox;
    @FXML
    private TextField rotationAmountTextField;

    //todo list of all objects that need to be initalized before calling: DirectoryReader, FileHandling, ConversionLogic, MainController

    public PhotoConversionPopupController() {
    }

    public void setHelperObjectClasses(DirectoryReader directoryReader, ConversionLogic conversionLogic, FileHandling fileHandling, MainController mainController) {
        this.directoryReader = directoryReader;
        this.conversionLogic = conversionLogic;
        this.fileHandling = fileHandling;
        this.mainController = mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    private void initialize() {
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
        List<Path> pathListToConvert = new ArrayList<>();
        //todo check if image selected is already of the file type being converted if so do not add it
        //todo create prompt to tell user that particular image/s are already of the selected file type
        //todo then have option to uncheck the images manually or automatically deselect all
        //todo have a method for checking file renaming
        //todo implement checks to ensure files will not be converted into file types that will fail

        //Checks for no photos selected otherwise they are added to the converted list
        if(showNoPhotosSelectedAlert()) {
            return;
        } else {
            pathListToConvert = conversionLogic.addImagesToConvertToList(radioButtonList, pathList);
        }

        //Checks for the resizing option being selected then checks for invalid characters in resolution TextFields
        //todo grey out all the resizing options if toResize is not selected, also make it not necessary to specify resolution heights if it is selected
        boolean toResize = toResizeCheckBox.isSelected();
        int finalPixelHeight = 0;
        int finalPixelWidth = 0;
        if (toResize) {
            if(showIncorrectResolutionSpecifiedAlert()) {
                return;
            } else {
                finalPixelHeight = Integer.valueOf(heightTextField.getText());
                finalPixelWidth = Integer.valueOf(widthTextField.getText());
            }
        }

        //Checks for valid output file type selected
        String fileFormat;
        if(showInvalidFileExtensionSpecifiedAlert()) {
            return;
        } else {
            fileFormat = outputFileFormatChoiceBox.getSelectionModel().getSelectedItem().toString();
            System.out.println("File format:" + fileFormat);
        }

        if(!conversionLogic.checkForValidDirectoryChosen(saveToCurrentDirectoryRadioButton, chosenDirectoryLabel)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please specify a valid directory");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return;
        }
        String amendedFilePath;
        if(saveToCurrentDirectoryRadioButton.isSelected()) {
            amendedFilePath = fileHandling.getPathInCorrectFormat(directoryReader.getDirectoryAsString());
            chosenDirectoryLabel.setText(amendedFilePath);
        } else {
             amendedFilePath = fileHandling.getPathInCorrectFormat(chosenDirectoryLabel.getText());
        }
        System.out.println("File path to save as: " + amendedFilePath);

        //Checks for rotation and checks for valid input
        boolean toRotate = toRotateCheckBox.isSelected();
        int rotationAmount = 0;
        if(toRotate && conversionLogic.doesContainInvalidInputForRotation(rotationAmountTextField.getText())) {
            showInvalidRotationAmountEntered();
            return;
        } else {
            rotationAmount = Integer.valueOf(rotationAmountTextField.getText());
        }

        //If the user wants to save to the current directory the chosen directory button should be updated

        //todo Record target filetype, destination path and final size
        conversionLogic.convertListOfFilesToConvert(pathListToConvert, fileFormat, amendedFilePath,
                toResize, finalPixelHeight, finalPixelWidth, toRotate, rotationAmount);
        /*
        if(succesesfulConversion) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Conversion successful!");
            alert.showAndWait().filter(response -> response == ButtonType.FINISH);
        }
        //todo add a popup to tell user that conversion is successful or not
        */
    }

    private void initializePopup() {
        outputFileFormatChoiceBox.getItems().addAll(directoryReader.getWritableFileExtensionList());
        outputFileFormatChoiceBox.setValue(".jpg");
        addListOfFilesToUserList();
    }

    public void showPopup() {
        initializePopup();
        stage.show();
    }

    public void setPopupStage(Stage popup) {
        this.stage = popup;
    }

    public void openFileDirectoryToSpecifyOutputPath() {
        //This needs to be called before calling DirectoryChooser
        fileHandling.getPhotoConversionStage(this.stage);
        //The below needs to be called before calling the DirectoryChooser
        fileHandling.setDirectoryReader(conversionLogic.getDirectoryReader());
        String toSetLabel = fileHandling.createDirectoryChoosingWindow();
        chosenDirectoryLabel.setText(toSetLabel);
    }

    //Will return true if Alert is shown
    private boolean showNoPhotosSelectedAlert() {
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
}
