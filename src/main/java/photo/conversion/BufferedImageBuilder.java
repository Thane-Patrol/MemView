package photo.conversion;

import javafx.util.Builder;

import java.awt.image.BufferedImage;
import java.io.File;

public class BufferedImageBuilder {
    //variables for resizing via number
    private int finalHeight;
    private int finalWidth;
    private boolean toResizeViaNumber;
    //variables for resizing via scaling factor
    private double scalingFactor;
    private boolean toResizeViaScalingFactor;
    //variables for applying rotation;
    private boolean toRotate;
    private double rotationFactor;
    //variables for applying watermark
    private boolean toApplyWatermark;
    private double watermarkScale;
    private String watermarkPosition;
    private File watermarkFile;
    private double opaquenessFactor;
    //object to be built and returned
    private BufferedImage bufferedImageToRtn;

    public BufferedImageBuilder(BufferedImage bufferedImage) {

    }

    private String prepareResizeViaNumberStatement(int finalWidth, int finalHeight) {
        return ".size(" + finalWidth + ", " + finalHeight + ")";
    }

    private String prepareResizeViaScalingFactor(double scalingFactor) {
        return ".scale(" + scalingFactor + ")";
    }

    private void setFinalHeight(int finalHeight) {
        this.finalHeight = finalHeight;
    }

    private void setFinalWidth(int finalWidth) {
        this.finalWidth = finalWidth;
    }

    private void setToResizeViaNumber(boolean toResizeViaNumber) {
        this.toResizeViaNumber = toResizeViaNumber;
    }

    private void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    private void setOpaquenessFactor(double opaquenessFactor) {
        this.opaquenessFactor = opaquenessFactor;
    }

    private void setWatermarkFile(File watermarkFile) {
        this.watermarkFile = watermarkFile;
    }

    private void setWatermarkPosition(String watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }

    private void setRotationFactor(double rotationFactor) {
        this.rotationFactor = rotationFactor;
    }

    private void setWatermarkScale(double watermarkScale) {
        this.watermarkScale = watermarkScale;
    }

    //todo implement a builder based off booleans that then build a final BufferedImage object for saving
}
