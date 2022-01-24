package com.example.memview;

import directory.handling.FileHandling;
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
    private MainController mainController;
    private Stage mainStage;
    private ConversionLogic conversionLogic;
    private FileHandling fileHandling;
    private List<RadioButton> radioButtonList;
    private List<Path> pathList;
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


    public PhotoConversionPopupController() {

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
    //This must be called before the addListOfFilesToUserList method
    public void setConversionLogic(ConversionLogic conversionLogic) {
        this.conversionLogic = conversionLogic;
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
        //todo check if image selected is already of the file type being converted if so do not add it
        //todo create prompt to tell user that particular image/s are already of the selected file type
        //todo then have option to uncheck the images manually or automatically deselect all
        //todo have a method for checking file renaming

        if(conversionLogic.checkForOneImageSelected(radioButtonList)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select photos to convert");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return;
        }

        conversionLogic.addImagesToConvertToList(radioButtonList, pathList);

        if(!conversionLogic.checkForCorrectInputInImageSize(heightTextField, widthTextField)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Illegal characters found - Please note that only whole numbers are allowed for resolution");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return;
        }

        if(!conversionLogic.checkForValidDirectoryChosen(saveToCurrentDirectoryRadioButton, chosenDirectoryLabel)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please specify a valid directory");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }
        if(saveToCurrentDirectoryRadioButton.isSelected()) {
            chosenDirectoryLabel.setText(conversionLogic.getDirectoryReader().getCurrentImage().getParent().toAbsolutePath().toString());
        }
        boolean toResize = toResizeCheckBox.isSelected();
        int finalPixelHeight = 0;
        int finalPixelWidth = 0;
        if (toResize) {
            finalPixelHeight = Integer.valueOf(heightTextField.getText());
            finalPixelWidth = Integer.valueOf(widthTextField.getText());
        }

        //todo grey out all the resizing options if toResize is not selected

        String amendedFilePath = fileHandling.getPathInCorrectFormat(chosenDirectoryLabel.getText());

        //If the user wants to save to the current directory the chosen directory button should be updated

        //todo Record target filetype, destination path and final size
        conversionLogic.convertListOfFilesToConvert(pathList, "jpg", amendedFilePath,
                toResize, finalPixelHeight, finalPixelWidth);

        /*
        if(succesesfulConversion) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Conversion successful!");
            alert.showAndWait().filter(response -> response == ButtonType.FINISH);
        }
        //todo add a popup to tell user that conversion is successful or not
        */
    }

    private void initializePopup() {
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
        fileHandling = mainController.getFileHandling();

        //This needs to be called before calling DirectoryChooser
        fileHandling.getPhotoConversionStage(this.stage);
        //The below needs to be called before calling the DirectoryChooser
        fileHandling.setDirectoryReader(conversionLogic.getDirectoryReader());
        String toSetLabel = fileHandling.createDirectoryChoosingWindow();
        chosenDirectoryLabel.setText(toSetLabel);
    }
}
