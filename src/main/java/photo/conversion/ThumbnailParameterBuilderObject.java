package photo.conversion;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailParameterBuilderObject {
    //variables for resizing via number
    private final int finalHeight;
    private final int finalWidth;
    private final boolean toResizeViaNumber;
    //variables for resizing via scaling factor
    private final double scalingFactor;
    private boolean toResizeViaScalingFactor;
    //variables for applying rotation;
    private final boolean toRotate;
    private final double rotationFactor;
    //variables for applying watermark
    private final boolean toApplyWatermark;
    private final double watermarkScale;
    private final Positions watermarkPosition;
    private final File watermarkFile;
    private final float opaquenessFactor;
    //object to be built and returned
    private BufferedImage bufferedImageToRtn;
    //builder for thumbnailParameter object
    private net.coobird.thumbnailator.builders.ThumbnailParameterBuilder thumbnailParameterBuilder;
    //object that stores all the parameters with getter methods to then use for a build
    private ThumbnailParameter thumbnailParameter;

    public ThumbnailParameterBuilderObject(BufferedImage bufferedImage, int finalWidth, int finalHeight, boolean toResizeViaNumber, double scalingFactor, boolean toResizeViaScalingFactor, boolean toRotate, double rotationFactor,
                                           boolean toApplyWatermark, double watermarkScale, Positions watermarkPosition, File watermarkFile, float opaquenessFactor) throws IOException {
        thumbnailParameterBuilder = new net.coobird.thumbnailator.builders.ThumbnailParameterBuilder();

        this.finalWidth = finalWidth;
        this.finalHeight = finalHeight;
        this.toResizeViaNumber = toResizeViaNumber;
        this.scalingFactor = scalingFactor;
        this.toRotate = toRotate;
        this.rotationFactor = rotationFactor;
        this.watermarkScale = watermarkScale;
        this.watermarkPosition = watermarkPosition;
        this.watermarkFile = watermarkFile;
        this.opaquenessFactor = opaquenessFactor;
        this.toApplyWatermark = toApplyWatermark;

         //todo add functionality for output quality
         //todo implement watermark and rotation functionality into the Thumbnailator github project in the thumbnailBuilder class
         //from here on user options are mutually exclusive due to the above deficiency in the Thumbnailator project

    }

    public BufferedImage createFinalImageToReturn(BufferedImage bufferedImage) {
        bufferedImageToRtn = bufferedImage;
        if(toResizeViaNumber) {
            thumbnailParameterBuilder.size(finalWidth, finalHeight);
        }

        if(toResizeViaScalingFactor) {
            thumbnailParameterBuilder.scale(scalingFactor);
        }

        //From here on options become mutually exclusive due to limitations in libraries.
        thumbnailParameter = thumbnailParameterBuilder.build();
        if(toRotate) {
            rotateImage(bufferedImage);

        } else if(toApplyWatermark) {
            applyWatermark(bufferedImage);

        } else {

        }

        return bufferedImageToRtn;
    }

    //returns true if image is to be scaled
    private boolean checkForScaleOrResize() {
        return thumbnailParameter.getSize().width != 0;
    }

    private BufferedImage rotateImage(BufferedImage bufferedImage) {
        try {
            if (checkForScaleOrResize()) {
                bufferedImageToRtn = Thumbnails.of(bufferedImage)
                        .scale(thumbnailParameter.getWidthScalingFactor(), thumbnailParameter.getHeightScalingFactor())
                        .rotate(rotationFactor)
                        .asBufferedImage();
            } else {
                bufferedImageToRtn = Thumbnails.of(bufferedImage)
                        .size(thumbnailParameter.getSize().width, thumbnailParameter.getSize().height)
                        .rotate(rotationFactor)
                        .asBufferedImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return bufferedImageToRtn;
    }

    private BufferedImage applyWatermark(BufferedImage bufferedImage) {
        try {
            if(checkForScaleOrResize()) {
                bufferedImageToRtn = Thumbnails.of(bufferedImage)
                        .scale(thumbnailParameter.getWidthScalingFactor(), thumbnailParameter.getHeightScalingFactor())
                        .watermark(watermarkPosition, ImageIO.read(watermarkFile), opaquenessFactor)
                        .asBufferedImage();
            } else {
                bufferedImageToRtn = Thumbnails.of(bufferedImage)
                        .size(thumbnailParameter.getSize().width, thumbnailParameter.getSize().height)
                        .watermark(watermarkPosition, ImageIO.read(watermarkFile), opaquenessFactor)
                        .asBufferedImage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImageToRtn;
    }
}
