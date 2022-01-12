package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class PhotoViewerApplicationLogic {

    private DirectoryReader directoryReader;
    private double vboxHeight;
    private HelloController helloController;

    public PhotoViewerApplicationLogic(DirectoryReader directoryReader, HelloController helloController) {
        this.directoryReader = directoryReader;
        this.vboxHeight = 250;
        this.helloController = helloController;

    }

    public String getPhotoSizeInUnits(Path imagePath) throws IOException {
        BasicFileAttributes fileAttributes = Files.readAttributes(imagePath, BasicFileAttributes.class);

        String fileSizeWithBytes;

        //Variables used to improve code readability and ease of future expandability
        long fileSize = fileAttributes.size();
        int KBCondition = 1000;
        int MBCondition = 1000_000;
        int GBCondition = 1000_000_000;


        if (fileSize >= KBCondition && fileSize < MBCondition) {
            fileSizeWithBytes = fileSize/1000 + " KB";

        } else if (fileSize >= MBCondition && fileSize < GBCondition) {
            fileSizeWithBytes = fileSize/1000_000 + " MB";

        } else if (fileSize >= GBCondition) {
            fileSizeWithBytes = fileSize/1000_000_000 + " GB";

        } else {
            fileSizeWithBytes = fileSize + " bytes";
        }

        return fileSizeWithBytes;
    }

    public HBox addPhotoThumbnailsToHBox(HBox hBox) {

        List<Path> filePaths = directoryReader.getListOfFilePaths();

        //Stream through this path, Create the labels, thumbnails and Vboxs to contain them and add all of them to the hbox then return it
        filePaths.stream().forEach(s -> {

            Label fileName = new Label();
            fileName.setText(s.getFileName().toString());

            BufferedImage imageSwing = null;
            try {
                imageSwing = ImageIO.read(s.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image image = SwingFXUtils.toFXImage(imageSwing, null);

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);

            //todo set height based upon reasonable screenbounds and/or current window size
            imageView.setFitHeight(200);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(imageView, fileName);

            //Set the mainImage view to the thumbnail when clicked on
            vBox.setOnMouseClicked(event -> {
                directoryReader.setCurrentImageIndex(s);
                helloController.gotoImageOnClick(image);
                    });

            //vBox.setId(String.valueOf(i.incrementAndGet()));
            hBox.getChildren().add(vBox);

            //To keep track of the height of a single vbox
        });

        return hBox;
    }

    //Used to get the region of the image underneath the main image
    public Image getImageUnderneathZoomBoxContainer(Pane zoomBox, ImageView mainImageView, MouseEvent mouseEvent) {

        //System.out.println("width: " + zoomBox.getTranslateX());
        //System.out.println("Height: " + zoomBox.getTranslateY());
//
        //System.out.println(zoomBox.getBoundsInParent().getCenterX());
//
        //System.out.println("Size of bounding box: " + "Height: " + zoomBox.getBoundsInParent().getHeight() + "width: " + zoomBox.getBoundsInParent().getWidth());

        //Integers used for specifying the region that is underneath the zoombox
        int topLeftPixel_X = (int) mouseEvent.getSceneX();
        int topLeftPixel_Y = (int) mouseEvent.getSceneY();

        WritableImage zoomedImageSection = new WritableImage(50, 50);

        PixelReader pixelReader = mainImageView.getImage().getPixelReader();
        WritablePixelFormat<IntBuffer> intBufferPixelFormat = PixelFormat.getIntArgbInstance();

        int[] pixelArray = new int[2500];
        pixelReader.getPixels(topLeftPixel_X, topLeftPixel_Y, 50, 50, intBufferPixelFormat, pixelArray, 0, 50);

        PixelWriter pixelWriter = zoomedImageSection.getPixelWriter();
        pixelWriter.setPixels(0, 0, 50, 50, intBufferPixelFormat, pixelArray, 0, 50);


        //todo better tracking of the mouse to create zoombox on section of image underneath
        //todo A robust check of whether the zoomBox is creating a box over the imageView, this is to prevent IOOBExceptions

        return zoomedImageSection;
    }

    public boolean isZoomBoxOverTheMainImage(Pane zoomBox, ImageView mainImageView) {
        return zoomBox.intersects(mainImageView.getBoundsInLocal());
    }

    public double getVboxHeight() {
        return vboxHeight;
    }
}
