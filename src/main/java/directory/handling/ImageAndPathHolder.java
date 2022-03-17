package directory.handling;

import javafx.scene.image.Image;

import java.nio.file.Path;

public class ImageAndPathHolder {
    private Image image;
    private Path path;

    public Image getImage() {
        return image;
    }

    public Path getPath() {
        return path;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
