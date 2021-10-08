package galleryUI;

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
    private int indexOfCurrentPhoto;
    private Rectangle2D screenBounds;

    //Todo add a Path object as a constructor so the image specified is opened on keypress
    //Todo handle exceptions that may be thrown, eg wrong filetype or nonsupported fileType
    public GalleryUI(File filePath) {
        inset = new Insets(20, 20, 20, 20);

        //Create a bufferedReader image to convert to javafx Image

        //Set the filePath as the image
        //todo remove this code
        this.image = new Image(filePath.toURI().toString());
        System.out.println(filePath.getAbsolutePath());

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
        //ToDo add full screen option
        this.imageView = new ImageView(this.image);

        //Add buttons to HBox and imageView to parentPane
        buttonBox.getChildren().addAll(backButton, spacer, nextButton);
        parentPane.getChildren().addAll(buttonBox, imageView);
        parentPane.setAlignment(buttonBox, Pos.CENTER);

        //Get the current screens size to allow a suitable size window to be shown

        screenBounds = Screen.getPrimary().getVisualBounds();
        System.out.println("Height:" + screenBounds.getHeight() + "Width: " + screenBounds.getWidth());

        //Create scene and add pane
        this.scene = new Scene(parentPane);
    }


    public void setFirstImage() {

    }

    public void setImageOnButtonPress(List<Path> pathList) {

        //Change photo to previous image on back button Press
        backButton.setOnMouseClicked(event -> {
            //Loop through pathList to find current position of photo
            //ToDo find a more efficient means to implement this
            for (int i = 0; i < pathList.size(); i++) {
                if (pathList.get(i).toString().equals(this.imageView.getImage().getUrl())) {

                }
            }
        });

        //Todo change this method to accept the input from an opened file
        //this.image.
        this.imageView.setImage(this.image);

    }

    public double getStageHeight() {
        return screenBounds.getHeight();
    }

    public double getStageWidth() {
        return screenBounds.getWidth();
    }

    public Scene getScene() { return this.scene;}




}