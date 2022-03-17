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
    private BufferedImage watermarkImage;
    private float opaquenessFactor;

    private Watermark watermark;

    //Builds based upon setters, leaving the logic for another class
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
            watermarkImage = Thumbnails.of(watermarkImage).scale(watermarkScale).asBufferedImage();
        } catch (IOException e ) {
            e.printStackTrace();
        }
        watermark = new Watermark(watermarkPosition, watermarkImage, opaquenessFactor);
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

    public void setWatermarkFile(BufferedImage watermarkImage) {
        this.watermarkImage = watermarkImage;
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


}
