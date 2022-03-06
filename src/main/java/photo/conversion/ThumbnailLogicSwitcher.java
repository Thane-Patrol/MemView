package photo.conversion;

import net.coobird.thumbnailator.geometry.Positions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThumbnailLogicSwitcher {
    private ThumbnailBuilder thumbnailBuilder;
    private Map<String, Boolean> parameterMap;
    private ParameterHolderHelper parameterHolderHelper;

    public ThumbnailLogicSwitcher(BufferedImage bufferedImage, ParameterHolderHelper parameterHolderHelper) {
        thumbnailBuilder = new ThumbnailBuilder(bufferedImage);
        parameterMap = new HashMap<>();
        this.parameterHolderHelper = parameterHolderHelper;
    }

    //This must be called after all the parameters and booleans have been set
    public BufferedImage getFinalImage() {
        setResize();
        setRotate();
        setScale();
        setWatermark();
        checkForNoResizing();
        return thumbnailBuilder.buildBufferedImage();
    }

    private void checkForNoResizing() {
        if(thumbnailBuilder.getPixelSize() == 0 && thumbnailBuilder.getScalingFactor() == 0) {
            thumbnailBuilder.setScalingFactor(1.0);
            thumbnailBuilder.setScale();
        }
    }

    private void setResize() {
        if(parameterMap.get("resize")) {
            thumbnailBuilder.setFinalWidth(parameterHolderHelper.getFinalWidth());
            thumbnailBuilder.setFinalHeight(parameterHolderHelper.getFinalHeight());
            thumbnailBuilder.setPixelSize();
        }
    }

    private void setScale() {
        if(parameterMap.get("scale")) {
            thumbnailBuilder.setScalingFactor(parameterHolderHelper.getScalingFactor());
            thumbnailBuilder.setScale();
        }
    }

    private void setWatermark() {
        if(parameterMap.get("watermark")) {
            //thumbnailBuilder.setWatermarkScale(parameterHolderHelper.get);
            thumbnailBuilder.setOpaquenessFactor(parameterHolderHelper.getOpaquenessFactor());
            thumbnailBuilder.setWatermarkFile(parameterHolderHelper.getWatermarkFile());
            thumbnailBuilder.setWatermarkPosition(parameterHolderHelper.getWatermarkPosition());
            thumbnailBuilder.createWatermarkImage();
            thumbnailBuilder.setWatermarkImage();

            System.out.println("watermark parameters: ");
            System.out.println("Opaquness factor: " + thumbnailBuilder.getOpaquenessFactor());
            System.out.println("Position: " + thumbnailBuilder.getWatermarkPosition().toString());
            System.out.println("Watermark file: " + thumbnailBuilder.getWatermarkFile().toString());

            //todo where i got up to:
            //todo debugging watermark application, the way forward would be to resize the watermark by a scaling factor
            //todo eg so the watermark is scaled relative to the final proportion of the parent image
        }
    }

    private void setRotate() {
        if(parameterMap.get("rotate")) {
            thumbnailBuilder.setRotationFactor(parameterHolderHelper.getRotationFactor());
            thumbnailBuilder.setRotate();
        }
    }

    public void addResizePixels(boolean toResizeViaPixels) {
        parameterMap.put("resize", toResizeViaPixels);
    }

    public void addScalePixels(boolean toScale) {
        parameterMap.put("scale", toScale);
    }

    public void addRotate(boolean toRotate) {
        parameterMap.put("rotate", toRotate);
    }

    public void addWatermark(boolean toWatermark) {
        parameterMap.put("watermark", toWatermark);
    }

    public void printAllSetParameters() {
        System.out.println("Printing Parameter Map: ");

        for(String string : parameterMap.keySet()) {
            System.out.println(string + ", value: " + parameterMap.get(string));
        }
    }









}
