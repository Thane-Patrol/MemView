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

package directory.handling;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileHandling {
    private static String OPERATING_SYSTEM;
    private static Stage photoConversionStage;
    private static DirectoryChooser directoryChooser;
    private static FileChooser fileChooser;

    public FileHandling(DirectoryReader directoryReader) {
        String s = System.getProperty("os.name").toLowerCase();

        if(s.contains("win")) {
            OPERATING_SYSTEM = "windows";
        }
        if(s.contains("mac")) {
            OPERATING_SYSTEM = "macos";
        }
        if (s.contains("linux")) {
            OPERATING_SYSTEM = "linux";
        }
        this.directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory");
        directoryChooser.setInitialDirectory(directoryReader.getCurrentImagePath().getParent().toFile().getAbsoluteFile());

        fileChooser = new FileChooser();
        fileChooser.setTitle("Select a watermark");
        fileChooser.setInitialDirectory(new File(directoryReader.getDirectoryAsString()));


    }

    public String createDirectoryChoosingWindowForOutput() {
        final File selectedDirectory = directoryChooser.showDialog(photoConversionStage);

        if(selectedDirectory != null) {
            selectedDirectory.getAbsolutePath();
        } else {
            return "No Directory Chosen";
        }
        return selectedDirectory.getAbsolutePath();
    }

    public String createFileChoosingWindowForWatermark() {
        final File selectedFile = fileChooser.showOpenDialog(photoConversionStage);

        if(selectedFile != null) {
            selectedFile.getAbsolutePath();
        } else {
            return "No Watermark Chosen";
        }
        System.out.println(selectedFile.getAbsolutePath());
        return selectedFile.getAbsolutePath();
    }

    public void getPhotoConversionStage(Stage stage) {
        photoConversionStage = stage;
    }

    public String getPathInCorrectFormat(String path) {
        String toRtn;
        if(OPERATING_SYSTEM.contains("win")) {
            toRtn = path + "\\";
        } else {
            toRtn = path + "/";
        }
        return toRtn;
    }



}
