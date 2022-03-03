package photo.conversion;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailBuilder {
    //Main object to build on and return
    private Thumbnails.Builder<BufferedImage> bufferedImageBuilder;
    //variables for resizing via number
    private int finalHeight;
    private int finalWidth;
    //variables for resizing via scaling factor
    private double scalingFactor;
    //variables for applying rotation;
    private double rotationFactor;
    //variables for applying watermark
    private double watermarkScale;
    private Positions watermarkPosition;
    private File watermarkFile;
    private float opaquenessFactor;

    private Watermark watermark;

    //Builds based upon setters, leaving the logic for another class
    //todo improve logic to reduce extraneous and repeated code
    public ThumbnailBuilder(BufferedImage bufferedImage) {
        bufferedImageBuilder = Thumbnails.of(bufferedImage);
    }

    public BufferedImage buildBufferedImage() {
        try {
            return bufferedImageBuilder.asBufferedImage();
        } catch(IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public void createWatermarkImage() {
        try {
            watermark = new Watermark(watermarkPosition, ImageIO.read(watermarkFile), opaquenessFactor);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setPixelSize() {
        bufferedImageBuilder = bufferedImageBuilder.size(finalWidth, finalHeight);
    }

    public void setScale() {
        bufferedImageBuilder = bufferedImageBuilder.scale(scalingFactor);
    }

    public void setRotate() {
        bufferedImageBuilder = bufferedImageBuilder.rotate(rotationFactor);
    }

    public void setWatermarkImage() {
        bufferedImageBuilder = bufferedImageBuilder.watermark(watermark);
    }

    public void setFinalHeight(int finalHeight) {
        this.finalHeight = finalHeight;
    }

    public void setFinalWidth(int finalWidth) {
        this.finalWidth = finalWidth;
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public void setRotationFactor(double rotationFactor) {
        this.rotationFactor = rotationFactor;
    }

    public void setWatermarkScale(double watermarkScale) {
        this.watermarkScale = watermarkScale;
    }

    public void setWatermarkPosition(Positions watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }

    public void setWatermarkFile(File watermarkFile) {
        this.watermarkFile = watermarkFile;
    }

    public void setOpaquenessFactor(float opaquenessFactor) {
        this.opaquenessFactor = opaquenessFactor;
    }

    public int getPixelSize() {
        return this.finalHeight;
    }

    public double getScalingFactor() {
        return this.scalingFactor;
    }

    public Positions getWatermarkPosition() {
        return watermarkPosition;
    }

    public float getOpaquenessFactor() {
        return opaquenessFactor;
    }

    public double getRotationFactor() {
        return rotationFactor;
    }

    public File getWatermarkFile() {
        return watermarkFile;
    }

    public int getFinalHeight() {
        return finalHeight;
    }

    public double getWatermarkScale() {
        return watermarkScale;
    }

    public int getFinalWidth() {
        return finalWidth;
    }

}
