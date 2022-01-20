package com.example.memview;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photo.conversion.ConversionLogic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PhotoConversionPopupController {
    private Stage stage;
    private HelloController mainController;
    private Stage mainStage;
    private ConversionLogic conversionLogic;
    private List<RadioButton> radioButtonList;
    private List<Path> pathList;
    @FXML
    private VBox radioButtonFileSelectVBox;


    public PhotoConversionPopupController() {

    }

    public void setMainController(HelloController mainController) {
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

        //todo change the logic so the extension is recorded from user and used as method parameter below as well as a path of output dir
        conversionLogic.convertListOfFilesToConvert(listOfSelectedFilePaths, "jpg", path);
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
