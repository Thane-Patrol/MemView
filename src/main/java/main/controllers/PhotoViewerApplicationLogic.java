package main.controllers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import directory.handling.DirectoryReader;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;

public class PhotoViewerApplicationLogic {
    private Pane zoomBoxPane;
    private final DirectoryReader directoryReader;
    private final PhotoViewerController photoViewerController;


    public PhotoViewerApplicationLogic(DirectoryReader directoryReader, PhotoViewerController photoViewerController) {
        this.directoryReader = directoryReader;
        this.photoViewerController = photoViewerController;

    }

    public String getPhotoSizeInUnits(Path imagePath) {
        BasicFileAttributes fileAttributes;
        try {
            fileAttributes = Files.readAttributes(imagePath, BasicFileAttributes.class);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

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

    public void addPhotoThumbnailsToHBox(HBox hBox) {
        List<Path> filePaths = directoryReader.getListOfFilePaths();

        //Stream through this path, Create the labels, thumbnails and Vboxs to contain them and add all of them to the hbox then return it
        filePaths.stream().forEach(s -> {

            Label fileName = new Label();
            fileName.setText(s.getFileName().toString());
            fileName.setMaxWidth(180);
            System.out.println("loading: " + s.getFileName().toString());

            BufferedImage finalImage;
            Image image;
            try {
                BufferedImage bufferedImage = ImageIO.read(s.toFile());
                finalImage = Thumbnails.of(bufferedImage).size(150, 150).keepAspectRatio(true).asBufferedImage();
                image = SwingFXUtils.toFXImage(finalImage, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }


            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(180);
            imageView.setFitHeight(150);
            VBox vBox = new VBox(imageView, fileName);
            vBox.setMaxHeight(Screen.getPrimary().getBounds().getHeight()/8);

            //Set the mainImage view to the thumbnail when clicked on
            vBox.setOnMouseClicked(event -> {
                directoryReader.setCurrentImageIndex(s);
                photoViewerController.gotoImageOnClick(directoryReader.loadImage());
                photoViewerController.updateMetadataLabel(directoryReader.getCurrentImagePath());
            });

            hBox.getChildren().add(vBox);
        });
    }

    private void setZoomBoxToRegionUnderMouse(MouseEvent event) {
        System.out.println(event.getTarget().toString());

        zoomBoxPane.setLayoutX(event.getSceneX());
        zoomBoxPane.setLayoutY(event.getSceneY());

        zoomBoxPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    private void setMaxZoomBoxSize() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        zoomBoxPane.setMaxHeight(bounds.getHeight() / 10);
        zoomBoxPane.setMaxWidth(bounds.getHeight() / 10);
    }

    private ImageView scaleImage(ImageView mainImageView) {
        ImageView imageView = new ImageView(mainImageView.getImage());
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(mainImageView.getFitHeight());
        imageView.setFitWidth(mainImageView.getFitWidth());
        return imageView;
    }

    private ImageView centerImageViewOverCursor(ImageView imageView, MouseEvent event) {
        ImageView zoomImage = imageView;
        zoomImage.setPreserveRatio(true);
        Bounds bounds = zoomBoxPane.getParent().getLayoutBounds();
        //zoomImage.setFitHeight(imageView.getFitHeight());
        //zoomImage.setFitWidth(imageView.getFitWidth());
        zoomImage.setLayoutX(bounds.getCenterX());
        zoomImage.setLayoutY(bounds.getCenterY());
        //imageView.setLayoutX(event.getSceneX());
        //imageView.setLayoutY(event.getSceneY());
        return zoomImage;
    }

    private ImageView getPixelsUnderneathZoomBox(ImageView imageView) {
        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) zoomBoxPane.getWidth();
        int height = (int) zoomBoxPane.getHeight();

        WritableImage zoomedImage = new WritableImage(width, height);
        PixelWriter writer = zoomedImage.getPixelWriter();

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color pixelColor = pixelReader.getColor(x, y);
                double red = pixelColor.getRed();
                double blue = pixelColor.getBlue();
                double green = pixelColor.getGreen();
                double opacity = pixelColor.getOpacity();

                writer.setColor(x, y, pixelColor);
            }
        }
        ImageView toRtn = new ImageView(zoomedImage);
        return toRtn;
    }

    public Pane setZoomedImage(ImageView mainImageView, MouseEvent mouseEvent) {
        setZoomBoxToRegionUnderMouse(mouseEvent);
        //ImageView imageView = scaleImage(mainImageView);
        //ImageView imageView = centerImageViewOverCursor(mainImageView, mouseEvent);
        //System.out.println("ImageView x, y coords: " + imageView.getLayoutX() + " y: " + imageView.getLayoutY());
        System.out.println("Zoombox x, y coords: " + zoomBoxPane.getLayoutX() + " y: " + zoomBoxPane.getLayoutY());
        //ImageView imageView = getPixelsUnderneathZoomBox(mainImageView);
        System.out.println("Adding image");
        //zoomBoxPane.getChildren().add(imageView);
        return zoomBoxPane;
    }

    public void hideZoomBox() {zoomBoxPane.setOpacity(0);}


    public Pane initializeZoomBox(Pane pane) {
        this.zoomBoxPane = pane;
        setMaxZoomBoxSize();
        zoomBoxPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return zoomBoxPane;
    }

    //Used to get the region of the image underneath the main image
    //todo implement
    public Image getImageUnderneathZoomBoxContainer(Pane zoomBox, ImageView mainImageView, MouseEvent mouseEvent) {
        return null;
    }

    public GeoLocation getGPSCoordinates(Path imagePath) {
        File imageFile = imagePath.toFile();
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
        } catch (ImageProcessingException | IOException imageProcessingException) {
            imageProcessingException.fillInStackTrace();
        }

        GeoLocation geoLocation = null;
        if(metadata == null) {
            return geoLocation;
        }
        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);


        for(GpsDirectory gpsDirectory : gpsDirectories) {
            geoLocation = gpsDirectory.getGeoLocation();

        }

        return geoLocation;
    }

    public boolean checkGeolocationForNull(GeoLocation geoLocation) {
        return geoLocation == null || geoLocation.isZero();
    }
}
