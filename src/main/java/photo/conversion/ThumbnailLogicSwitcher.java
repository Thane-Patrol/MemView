/*
 * MemView - a simple photo viewer and converter written in Java
 *     Copyright (C) 2021 Hugh Mandalidis
 *     Contact: mandalidis.hugh@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>
 */

package photo.conversion;

import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailLogicSwitcher {
    private final BufferedImage bufferedImage;
    private ThumbnailBuilder thumbnailBuilder;
    private Map<String, Boolean> parameterMap;
    private ParameterHolderHelper parameterHolderHelper;

    public ThumbnailLogicSwitcher(BufferedImage bufferedImage, ParameterHolderHelper parameterHolderHelper) {
        this.bufferedImage = bufferedImage;
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

            thumbnailBuilder.setWatermarkScale(parameterHolderHelper.getWatermarkScale());
            thumbnailBuilder.setOpaquenessFactor(parameterHolderHelper.getOpaquenessFactor());
            thumbnailBuilder.setWatermarkFile(resizeWatermarkImage());
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

    //resizes the watermark to the same size as the parent image
    private BufferedImage resizeWatermarkImage() {
        int heightToResizeTo = bufferedImage.getHeight();
        BufferedImage bufferedImageOfWatermark = null;

        try {
            bufferedImageOfWatermark = ImageIO.read(parameterHolderHelper.getWatermarkFile());
            double scalingFactor = (double) heightToResizeTo / bufferedImageOfWatermark.getHeight();
            bufferedImageOfWatermark = Thumbnails.of(bufferedImageOfWatermark).scale(scalingFactor).asBufferedImage();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImageOfWatermark;
    }









}
