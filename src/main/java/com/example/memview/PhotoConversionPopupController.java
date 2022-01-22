package com.example.memview;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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
        for(RadioButton radioButton : radioButtonList) {
            if(radioButton.isSelected()) {
                for(Path path : pathList) {
                    if(path.getFileName().toString().equals(radioButton.getText())) {
                        listOfSelectedFilePaths.add(path);
                    }
                }
            }
        }
        String path = "/home/hugh/Documents/Development/javaMemView/output_dir/";

        if(!conversionLogic.checkForCorrectInputInImageSize(heightTextField, widthTextField)) {
            //todo create popup here to inform user of incorrect input then break
            return;
        }

        int finalPixelHeight = Integer.valueOf(heightTextField.getText());
        int finalPixelWidth = Integer.valueOf(widthTextField.getText());

        boolean toResize = true;
        boolean toConvertFileType = true;

        //todo Record target filetype, destination path and final size
        conversionLogic.convertListOfFilesToConvert(listOfSelectedFilePaths, "jpg", path,
                toResize, finalPixelHeight, finalPixelWidth);
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
