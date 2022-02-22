package photo.conversion;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestBuilderObject {
    //Main object to build on and return
    private BufferedImage bufferedImage;
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

    //Builds based upon setters, leaving the logic for another class
    //todo improve logic to reduce extraneous and repeated code
    public TestBuilderObject(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage buildResizedViaPixelsBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).size(finalWidth, finalHeight).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildResizedViaScalingBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).scale(scalingFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildWaterMarkedBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).scale(1.0).watermark(watermarkPosition, ImageIO.read(watermarkFile), opaquenessFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildRotatedBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).rotate(rotationFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildResizeViaScaleAndRotateBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).scale(scalingFactor).rotate(rotationFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildResizeViaScaleAndWatermarkBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).scale(scalingFactor).watermark(watermarkPosition, ImageIO.read(watermarkFile), opaquenessFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildResizeViaPixelsAndRotateBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).size(finalWidth, finalHeight).rotate(rotationFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
    }

    public BufferedImage buildResizeViaPixelsAndWatermarkBufferedImage() {
        BufferedImage toRtn;
        try {
            toRtn = Thumbnails.of(bufferedImage).size(finalWidth, finalHeight).watermark(watermarkPosition, ImageIO.read(watermarkFile), opaquenessFactor).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return toRtn;
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
}
