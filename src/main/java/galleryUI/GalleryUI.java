package galleryUI;

import directory.handling.DirectoryReader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.Scene;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class GalleryUI {

    //Reusable Inset
    private Insets inset;
    private Scene scene;
    private Image image;
    private ImageView imageView;
    private Button backButton;
    private Button nextButton;

    //Todo add a Path object as a constructor so the image specified is opened on keypress
    //Todo handle exceptions that may be thrown, eg wrong filetype or nonsupported fileType
    public GalleryUI(File filePath) {
        inset = new Insets(20, 20, 20, 20);

        //Create a bufferedReader image to convert to javafx Image

        //Set the filePath as the image
        //todo remove this code
        this.image = new Image(filePath.toURI().toString());
        //System.out.println(filePath.getAbsolutePath());

        //Create pane and set layout
        StackPane parentPane = new StackPane();
        parentPane.setPadding(inset);
        parentPane.setPrefSize(640, 480);

        //Create button box and add Pane as a spacer
        HBox buttonBox = new HBox();

        Pane spacer = new Pane();
        buttonBox.setHgrow(spacer, Priority.ALWAYS);

        //Create back and forward buttons
        //ToDo add graphics to buttons
        this.nextButton = new Button(">");
        this.backButton = new Button("<");

        //Create imageView for main Gallery
        this.imageView = new ImageView(this.image);

        //Add buttons to HBox and imageView to parentPane
        buttonBox.getChildren().addAll(backButton, spacer, nextButton);
        parentPane.getChildren().addAll(imageView, buttonBox);
        parentPane.setAlignment(buttonBox, Pos.CENTER);


        //Create scene and add pane
        this.scene = new Scene(parentPane);
    }


    public void setFirstImage() {

    }

    public void setPreviousImageOnButtonPress(Image image) {

        //Todo change this method to accept the input from an opened file
        //this.image.
        this.imageView.setImage(this.image);

    }

    public void setNextImageOnButtonPress(Image image) {

        //Todo change this method to accept the input from an opened file
        //this.image.
        this.imageView.setImage(image);

    }

    public Button getNextButton() {
        return this.nextButton;
    }

    public Button getBackButton() {
        return this.backButton;
    }

    public Scene getScene() { return this.scene;}




}