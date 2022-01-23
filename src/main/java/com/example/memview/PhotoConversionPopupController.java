package com.example.memview;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photo.conversion.ConversionLogic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PhotoConversionPopupController {
    private Stage stage;
    private MainController mainController;
    private Stage mainStage;
    private ConversionLogic conversionLogic;
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
        List<Path> listOfSelectedFilePaths = new ArrayList<>();

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

        //todo get input from user to replace this generic string once debugging is done
        String path = "/home/hugh/Documents/Development/javaMemView/output_dir/";

        if(!conversionLogic.checkForCorrectInputInImageSize(heightTextField, widthTextField)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Illegal characters found - Please note that only whole numbers are allowed for resolution");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            return;
        }

        int finalPixelHeight = Integer.valueOf(heightTextField.getText());
        int finalPixelWidth = Integer.valueOf(widthTextField.getText());

        boolean toResize = true;
        boolean toConvertFileType = true;

        //todo Record target filetype, destination path and final size
        conversionLogic.convertListOfFilesToConvert(listOfSelectedFilePaths, "jpg", path,
                toResize, finalPixelHeight, finalPixelWidth);
        //todo add a popup to tell user that conversion is successful or not

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
}
