package photo.conversion;

import net.coobird.thumbnailator.ThumbnailParameter;

import java.awt.image.BufferedImage;
import java.io.File;

public class ThumbnailParameterBuilder {
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
    //builder for thumbnailParameter object
    private net.coobird.thumbnailator.builders.ThumbnailParameterBuilder thumbnailParameterBuilder;
    //object that stores all the parameters with getter methods to then use for a build
    private ThumbnailParameter thumbnailParameter;

    public ThumbnailParameterBuilder(BufferedImage bufferedImage) {
        thumbnailParameterBuilder = new net.coobird.thumbnailator.builders.ThumbnailParameterBuilder();
         if(toResizeViaNumber) {
             thumbnailParameterBuilder.size(finalWidth, finalHeight);
         }

         if(toResizeViaScalingFactor) {
             thumbnailParameterBuilder.scale(scalingFactor);
         }

         //todo add functionality for outputquality
        //todo Where i got up to - creating a thumbnail paramater builder but realizing that the built in thumbnail parameter builder is a bit limited
        //todo best to use it for resizing, output format, output quality and filters. It cannot be used for applying watermarks and rotation

         //todo implement watermark and rotation functionality into the Thumbnailator github project in the thumbnailBuilder class
         /*
         if(toRotate) {
             thumbnailParameterBuilder.
         } */
        /*
        if(toApplyWatermark) {

        } */

    }


    //File is set as constructor as it can be used to reference the image to obtain height, etc
    public static class BufferedImageBuilder {
        private int resizeHeight;
        private int resizeWidth;
        private double scalingFactor;
        private String watermarkPosition;
        private File watermarkFile;
        private double opaquenessFactor;
        private double watermarkScale;
        private double rotationAmount;


        public BufferedImageBuilder() {
        }

        public BufferedImageBuilder withResizeHeightAndWidth(int resizeHeight, int resizeWidth) {
            this.resizeHeight=resizeHeight;
            this.resizeWidth=resizeWidth;
            return this;
        }

        public BufferedImageBuilder withScalingFactor(double scalingFactor) {
            this.scalingFactor =  scalingFactor;
            return this;
        }

        public BufferedImageBuilder withWatermark(String watermarkPosition, File watermarkFile, double opaquenessFactor, double watermarkScale) {
            this.watermarkPosition = watermarkPosition;
            this.watermarkFile = watermarkFile;
            this.opaquenessFactor = opaquenessFactor;
            this.watermarkScale = watermarkScale;
            return this;
        }

        public BufferedImageBuilder withRotation(double rotationAmount) {
            this.rotationAmount = rotationAmount;
            return this;
        }
    }
}
