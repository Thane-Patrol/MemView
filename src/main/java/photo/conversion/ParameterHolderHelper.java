package photo.conversion;

import net.coobird.thumbnailator.geometry.Positions;

import java.io.File;

public class ParameterHolderHelper {
    private int finalHeight;
    private int finalWidth;
    private double scalingFactor;
    private double rotationFactor;
    private Positions watermarkPosition;
    private File watermarkFile;
    private float opaquenessFactor;
    private boolean toResizeViaPixels;
    private boolean toScale;
    private boolean toRotate;
    private boolean toWatermark;
    private String extensionToSaveAs;
    private String outputPath;

    public ParameterHolderHelper() {
        toResizeViaPixels = false;
        toScale = false;
        toRotate = false;
        toWatermark = false;
    }

    public boolean isToResizeViaPixels() {
        return toResizeViaPixels;
    }

    public boolean isToRotate() {
        return toRotate;
    }

    public boolean isToScale() {
        return toScale;
    }

    public boolean isToWatermark() {
        return toWatermark;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getExtensionToSaveAs() {
        return extensionToSaveAs;
    }

    public void setExtensionToSaveAs(String extensionToSaveAs) {
        this.extensionToSaveAs = extensionToSaveAs;
    }

    public void setToResizeViaPixels(boolean toResizeViaPixels) {
        this.toResizeViaPixels = toResizeViaPixels;
    }

    public void setToRotate(boolean toRotate) {
        this.toRotate = toRotate;
    }

    public void setToScale(boolean toScale) {
        this.toScale = toScale;
    }

    public void setToWatermark(boolean toWatermark) {
        this.toWatermark = toWatermark;
    }

    public void setOpaquenessFactor(float opaquenessFactor) {
        this.opaquenessFactor = opaquenessFactor;
    }

    public void setWatermarkFile(File watermarkFile) {
        this.watermarkFile = watermarkFile;
    }

    public void setWatermarkPosition(Positions watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }

    public void setRotationFactor(double rotationFactor) {
        this.rotationFactor = rotationFactor;
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public void setFinalHeight(int finalHeight) {
        this.finalHeight = finalHeight;
    }

    public void setFinalWidth(int finalWidth) {
        this.finalWidth = finalWidth;
    }

    public double getRotationFactor() {
        return rotationFactor;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public File getWatermarkFile() {
        return watermarkFile;
    }

    public float getOpaquenessFactor() {
        return opaquenessFactor;
    }

    public int getFinalHeight() {
        return finalHeight;
    }

    public int getFinalWidth() {
        return finalWidth;
    }

    public Positions getWatermarkPosition() {
        return watermarkPosition;
    }
}
