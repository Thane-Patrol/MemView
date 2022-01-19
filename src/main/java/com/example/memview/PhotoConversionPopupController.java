package com.example.memview;

import javafx.fxml.FXML;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PhotoConversionPopupController {
    private Popup popup;
    private Stage mainStage;
    private HelloController mainController;

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

    @FXML
    private void hidePopup() {
        popup.hide();
    }

    public void showPopup() {
        popup.show(mainStage, 50, 50);
    }

    public void setPopup(Popup popup) {
        this.popup = popup;
    }
}
