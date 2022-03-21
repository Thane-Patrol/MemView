package main.controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class ZoomBoxLogic {
    private ImageView zoomedImage;
    private Pane zoomBoxPane;
    private final Rectangle2D bounds = Screen.getPrimary().getBounds();
    private final double screenWidth = bounds.getWidth();
    private final double screenHeight = bounds.getHeight();

    public Pane initializeZoomBox(Pane zoomBoxPane) {
        this.zoomedImage = new ImageView();
        this.zoomBoxPane = zoomBoxPane;
       // this.zoomBoxPane.getChildren().add(this.zoomedImage);
        //scaleZoomBox(event);
        return zoomBoxPane;
    }

    public Pane initializeZoomBoxOnUserClick(MouseEvent event) {
        zoomedImage.setPreserveRatio(true);
        scaleZoomBox(event);
        zoomBoxPane.setLayoutX(event.getSceneX());
        zoomBoxPane.setLayoutY(event.getSceneY());
        zoomBoxPane.getChildren().add(zoomedImage);
        return zoomBoxPane;
    }

    private void scaleZoomBox(MouseEvent event) {
        zoomedImage.setScaleX(4);
        zoomedImage.setScaleY(4);
        Rectangle2D viewport = new Rectangle2D(event.getSceneX() + 85, event.getSceneY() + 85, 85, 85);
        zoomedImage.setViewport(viewport);
    }

    public void moveOnMouseDragged(MouseEvent event) {
        zoomBoxPane.setTranslateX(event.getSceneX() - zoomBoxPane.getLayoutX() - 0.5 * zoomBoxPane.getWidth());
        zoomBoxPane.setTranslateY(event.getSceneY() - zoomBoxPane.getLayoutY() - 0.5 * zoomBoxPane.getHeight());
        scaleZoomBox(event);
        event.consume();
    }

    public void hideZoomBox() {
        zoomedImage.setImage(null);
        zoomBoxPane.getChildren().remove(zoomedImage);
    }
}
