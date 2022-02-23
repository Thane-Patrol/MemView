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
        return thumbnailBuilder.buildBufferedImage();

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
            thumbnailBuilder.setOpaquenessFactor(parameterHolderHelper.getOpaquenessFactor());
            thumbnailBuilder.setWatermarkFile(parameterHolderHelper.getWatermarkFile());
            thumbnailBuilder.setWatermarkPosition(parameterHolderHelper.getWatermarkPosition());
            thumbnailBuilder.createWatermarkImage();
            thumbnailBuilder.setWatermarkImage();
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









}
