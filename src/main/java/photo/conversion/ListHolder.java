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

package photo.conversion;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ListHolder {
   private List<RadioButton> radioButtonList = new ArrayList<>();
   private List<HBox> hBoxList = new ArrayList<>();
   private List<Path> filePathList;
   
    public void setHBoxList(List<HBox> hBoxList) {
        this.hBoxList = hBoxList;
    }

    public void setRadioButtonList(List<RadioButton> radioButtonList) {
        this.radioButtonList = radioButtonList;
    }

    public void setFilePathList(List<Path> filePathList) {
        this.filePathList = filePathList;
    }

    public List<Path> getFilePathList() {
        return filePathList;
    }

    public List<HBox> getHBoxList() {
        return hBoxList;
    }

    public List<RadioButton> getRadioButtonList() {
        return radioButtonList;
    }
    
    
}
