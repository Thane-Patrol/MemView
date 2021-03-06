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

package directory.handling;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.annotation.Testable;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectoryReaderTest {

    private List<Path> fileNames = new ArrayList<>();
    private List<String> readableExtensionList = new ArrayList<>();
    private List<String> writableExtensionList = new ArrayList<>();
    private final Path outOfBoundsImagePath = Paths.get("image.Resources/outOfBoundsImage");
    private int currentFileIndex;
    
    
    private void initializeFileList() {
        System.out.println("iniitalizeFileList method called");
        File originalFilePath = new File("src/test/test.resources/images/1.png");

        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(originalFilePath.toPath().getParent())) {
            System.out.println("Try block called");
            for(Path photos : directoryStream) {
                String unknownCapsExtension = FilenameUtils.getExtension(photos.toString());
                String lowerCaseExtension = unknownCapsExtension.toLowerCase();
                String fileNameAsString = FilenameUtils.removeExtension(photos.toString());
                String amendedPhoto = fileNameAsString + "." + lowerCaseExtension;
                Path amendedPhotoPath = Paths.get(amendedPhoto);

                if(!Files.isDirectory(photos) && testFileIsAPhoto(amendedPhotoPath)) {
                    int OGFileNameSize = fileNames.size();
                    fileNames.add(amendedPhotoPath);
                    int finFileNameSize = fileNames.size();
                    Assertions.assertTrue(OGFileNameSize < finFileNameSize);
                    System.out.println("File added: " + amendedPhotoPath.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testAddPhotosToList() {
        initializeFileList();
        
        System.out.println("filename size = " + fileNames.size() );
        fileNames.stream().forEach(s -> System.out.println(s.getFileName()));
        Assertions.assertTrue(fileNames.size() == 10);
    }

    @Test
    public Path testGetCurrentImage() {
        initializeFileList();
        return fileNames.get(currentFileIndex);
    }

    @Test
    public Path testGetPreviousImage() {
        initializeFileList();
        int originalFileIndex = currentFileIndex;

        if(fileNames.size() == 1) {
            return outOfBoundsImagePath;
        } else if(-1 == currentFileIndex - 1) {
            return outOfBoundsImagePath;
        } else if (fileNames.size() == 0) {
            return outOfBoundsImagePath;
        } else {
            Path toRtn = fileNames.get(currentFileIndex - 1);
            currentFileIndex--;

            int finFileIndex = currentFileIndex;
            boolean toAssert1 = originalFileIndex - finFileIndex == 1;
            boolean toAssert2 = originalFileIndex > finFileIndex;
            boolean toAssert3 = finFileIndex >= 0;
            boolean toAssert4 = finFileIndex < fileNames.size();

            Assertions.assertTrue(toAssert1 && toAssert2 && toAssert3 && toAssert4);
            return toRtn;
        }

    }

    @Test
    public Path testGetNextImage() {
        initializeFileList();
        int originalFileIndex = currentFileIndex;
        if(fileNames.size() == 1) {
            return outOfBoundsImagePath;

        } else if (fileNames.size() == currentFileIndex + 1) {
            return outOfBoundsImagePath;

        } else if (fileNames.size() == 0) {
            return outOfBoundsImagePath;

        } else {
            Path toRtn = fileNames.get(currentFileIndex + 1);
            currentFileIndex++;

            int finFileIndex = currentFileIndex;
            boolean toAssert1 = finFileIndex - originalFileIndex == 1;
            boolean toAssert2 = originalFileIndex < finFileIndex;
            boolean toAssert3 = finFileIndex >= 0;
            boolean toAssert4 = finFileIndex < fileNames.size();

            Assertions.assertTrue(toAssert1 && toAssert2 && toAssert3 && toAssert4);
            return toRtn;
        }
    }

    public void testSetCurrentImageIndex() {
        Path imagePath = Paths.get("src/test/test.resources/images/2.png");

        for (int i = 0; i < fileNames.size(); i++) {
            if(imagePath.equals(fileNames.get(i))) {
                currentFileIndex = i;
            }
        }
    }

    @Test
    public String getPhotoExtension() {
        String toRtn = FilenameUtils.getExtension(testGetCurrentImage().toString());
        Assertions.assertTrue(readableExtensionList.contains(toRtn));
        return toRtn;
    }

    @Test
    public Image testLoadImageFromPath(Path imagePath) {
        Image image;
        try {
            File imageFile = imagePath.toFile();

            if(this.getPhotoExtension().equals("gif")) {
                image = new Image(imageFile.toURI().toString());
            } else {
                image = SwingFXUtils.toFXImage(ImageIO.read(imageFile), null);
            }
            return image;

        }  catch(IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    @Test
    public Image testLoadImage() {
        initializeFileList();
        
        Image image;
        try {
            File firstImagePath = fileNames.get(currentFileIndex).toFile();

            if(this.getPhotoExtension().equals("gif")) {
                image = new Image(firstImagePath.toURI().toString());
            } else {
                image = SwingFXUtils.toFXImage(ImageIO.read(firstImagePath), null);
            }

            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testGetFirstFileIndex() {
        initializeFileList();
        File originalFilePath = new File("src/test/test.resources/images/2.png");
        //workaround for Maven automated testing failing

        for(int i = 0; i < fileNames.size(); i++) {
            if(fileNames.get(i).equals(originalFilePath.toPath())) {
                currentFileIndex = i;
            }
        }
        
        boolean testAssertion1 = fileNames.contains(fileNames.get(currentFileIndex));
        System.out.println("assertion 2: " + testAssertion1);
        Assertions.assertTrue(testAssertion1);
    }

    @Test
    public boolean testFileIsAPhoto(Path photoPath) {
        for(String fileExtensionName : readableExtensionList) {
            if(photoPath.getFileName().toString().contains(fileExtensionName)) {
                Assertions.assertTrue(photoPath.getFileName().toString().contains(fileExtensionName));
                return true;
            }
        }
        return false;
    }

    @Test
    public void testAddReadableExtensionList() {
        readableExtensionList.add(0, ".png");
        readableExtensionList.add(1, ".jpg");
        readableExtensionList.add(2, ".bmp");
        readableExtensionList.add(3, ".cur");
        readableExtensionList.add(4, ".ico");
        readableExtensionList.add(5, ".pict");
        readableExtensionList.add(6, ".pntg");
        readableExtensionList.add(7, ".pam");
        readableExtensionList.add(8, ".pbm");
        readableExtensionList.add(9, ".pgm");
        readableExtensionList.add(10, ".ppm");
        readableExtensionList.add(11, ".pfm");
        readableExtensionList.add(12, ".psd");
        readableExtensionList.add(12, ".psb");
        readableExtensionList.add(13, ".tga");
        readableExtensionList.add(15, ".webp");
        readableExtensionList.add(16, ".hdr");
        readableExtensionList.add(17, ".gif");
        readableExtensionList.add(18, ".tiff");
        readableExtensionList.add(19, "tif");
        readableExtensionList.add(20, ".pcx");
        readableExtensionList.add(21, ".dcx");
        readableExtensionList.add(22, ".sgi");

        System.out.println("readableExtension size =" + readableExtensionList.size());
        //List will be double the size it should be
        Assertions.assertTrue(readableExtensionList.size() == 23);
    }

    @Test
    public void testAddWriteableFileExtensionsToList() {
        writableExtensionList.add(0, ".bmp");
        writableExtensionList.add(1, ".ico");
        writableExtensionList.add(2, ".icns");
        writableExtensionList.add(3, ".jpg");
        writableExtensionList.add(4, ".pict");
        writableExtensionList.add(5, ".pnm");
        writableExtensionList.add(6, ".psd");
        writableExtensionList.add(7, ".tga");
        writableExtensionList.add(8, ".tiff");

        System.out.println("writableExtensions size = " + writableExtensionList.size());
        //List will be double the size it should be
        Assertions.assertTrue(writableExtensionList.size() == 9);
    }
}
