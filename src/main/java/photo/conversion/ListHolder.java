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
