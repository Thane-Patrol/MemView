package com.example.memview;

import directory.handling.DirectoryReader;
import galleryUI.GalleryUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloApplication extends Application {

    private static Image outOfBoundsImage;
    private static Image firstImage;

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        /*
        System.out.println("Give a file path");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

         */

        //Todo get the filename in a whitespace trimmed format to pass as a constructor to the DirectoryReader class
        //Create DirectoryReader to index files before the GalleryUI is started
        //OSX path /Users/hugh/Desktop/memView/BackGardenEntire.png
        //Windows path: D:\\javaMemView\\gtest(1).jpg
        DirectoryReader directoryReader = new DirectoryReader(firstImage.getUrl().toString());

        //Create new GalleryUI on application start
        GalleryUI galleryUI = new GalleryUI(directoryReader.getPath());

        Button nextButton = galleryUI.getNextButton();
        Button backButton = galleryUI.getBackButton();

        new AnimationTimer() {

            @Override
            public void handle(long now) {

                nextButton.setOnMouseClicked(event -> {
                    Image image = new Image(directoryReader.getNextImage().toUri().toString());
                    galleryUI.setImageOnButtonPress(image);
                });

                backButton.setOnMouseClicked(event -> {
                    Image image = new Image(directoryReader.getPreviousImage().toUri().toString());
                    galleryUI.setImageOnButtonPress(image);
                });
            }
        }.start();


        stage.setTitle("Hello!");
        stage.setScene(galleryUI.getScene());

        //Set to start as Maximised Window to prevent weird multiMonitor issues
        stage.setMaximized(true);

        stage.show();
    }

    public static <WindowsPath> void main(String[] args) {
        outOfBoundsImage = new Image(Paths.get("image.Resources/testOutOfBoundsImage.png").toUri().toString());

        //ToDo get rid of this debugging
        //toDo sort out why the filepath that is being passed to the args array is not translating into a readable file Path for the DirectoryReader class
        for(String toPrint : args) {
            System.out.println(toPrint);
        }
        Path filePath = Paths.get(args[0]);

        System.out.println("File Path given to String[] args: " + filePath.toString());

        firstImage = new Image(filePath.toUri().toString());

        launch();
    }
}