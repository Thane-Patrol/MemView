/*
 * MemView - a simple photo viewer and converter written in Java
 *     Copyright (C) 2021 Hugh Mandalidis
 *     Contact: mandalidis.hugh@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>
 */

package main.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Stage mainStage = new Stage();

        //main FXML file
        FXMLLoader fxmlLoaderMain = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoaderMain.load(), 640, 480);
        PhotoViewerController controller = fxmlLoaderMain.getController();
        controller.setHostServices(this.getHostServices());
        controller.initializeSceneDependentComponents();

        //popup FXML file for conversion of photos
        //This needs to be called before the main controller
        Stage popupStage = new Stage();
        FXMLLoader fxmlLoaderPopupConversion = new FXMLLoader(HelloApplication.class.getResource("photo-conversion.fxml"));
        Scene popupScene = new Scene(fxmlLoaderPopupConversion.load(), 640, 850);
        PhotoConversionController photoConversionController = fxmlLoaderPopupConversion.getController();

        //These two methods need to be called in this order
        controller.setPhotoConversionPopupController(photoConversionController);
        controller.setServicesForPhotoController();

        popupStage.setScene(popupScene);
        photoConversionController.setPopupStage(popupStage);
        popupStage.setTitle("Photo Resizing");

        mainStage.setTitle("MemView");
        mainStage.setScene(scene);

        //Set to start as Maximised Window to prevent weird multiMonitor issues
        mainStage.setMaximized(true);
        mainStage.show();
        scene.getRoot().requestFocus();
    }

    public static void main(String[] args) {

        launch();
    }
}