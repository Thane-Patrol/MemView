package com.example.memview;

import directory.handling.DirectoryReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ApplicationLogic {

    private DirectoryReader directoryReader;
    private double vboxHeight;

    public ApplicationLogic(DirectoryReader directoryReader) {
        this.directoryReader = directoryReader;
        this.vboxHeight = 250;
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

        //Use an integer counter to create unique names for the vboxes, also helpful for potentially keeping track of which thumbnail is which
        AtomicInteger i = new AtomicInteger();

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

            //vBox.setId(String.valueOf(i.incrementAndGet()));
            hBox.getChildren().add(vBox);

            //To keep track of the height of a single vbox
        });

        return hBox;
    }

    public double getVboxHeight() {
        return vboxHeight;
    }
}
