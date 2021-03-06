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
    private float watermarkScale;
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

    public void setWatermarkScale(float watermarkScale) {
        this.watermarkScale = watermarkScale;
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

    public float getWatermarkScale() {
        return watermarkScale;
    }

    public boolean checkForAnythingTransformationExceptFiles() {
        if(toWatermark) {
            return false;
        } else if(toRotate) {
            return false;
        } else if(toScale) {
            return false;
        } else return !toResizeViaPixels;
    }
}
