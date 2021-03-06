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
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DirectoryReader {

    private final List<Path> fileNames;
    private final List<Image> imageList;
    private int currentFileIndex;
    private final List<String> readableExtensionList;
    private final List<String> writableFileExtensionList;
    private final Path outOfBoundsImagePath;

    // Creates the DirectoryReader object to index all the files in the directory of the open file
    // THe originalFile object is the absolute Path of the file opened
    public DirectoryReader(String unsanitizedFileName) {
        outOfBoundsImagePath = Paths.get("src/main/resources/image.Resources/41nrqdLzutL._AC_SY580_.jpg");

        String sanitisedFileName = unsanitizedFileName.replaceAll("//s","");

        File originalFilePath = new File(sanitisedFileName);
        fileNames = new ArrayList<>();
        imageList = new ArrayList<>();
        //Readable File Extensions
        readableExtensionList = FXCollections.observableArrayList();
        addReadableExtensionList();
        //Writable File Extensions
        writableFileExtensionList = FXCollections.observableArrayList();
        addWriteableFileExtensionsToList();

        addPhotosToPathList(originalFilePath);
        getFirstPathIndex(originalFilePath);


    }

    private void addPhotosToPathList(File originalFilePath) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(originalFilePath.toPath().getParent())) {
            //For each loop to loop through all photos and add them to the fileNames list and Image list
            for (Path photos : directoryStream) {
                if(!Files.isDirectory(photos) && fileIsAPhoto(photos)) {
                    fileNames.add(photos);
                    imageList.add(loadGIFRespectingImage(photos));
                }
            }
        } catch (Exception e) {
            System.out.println("Error Message in addPhotosToList method ");
            System.out.println(e.getMessage());
        }
    }


    private void getFirstPathIndex(File originalFilePath) {
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).equals(originalFilePath.toPath())) {
                currentFileIndex = i;
            }
        }
    }

    private void addReadableExtensionList() {
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
        readableExtensionList.add(19, ".tif");
        readableExtensionList.add(20, ".pcx");
        readableExtensionList.add(21, ".dcx");
        readableExtensionList.add(22, ".sgi");
    }

    private void addWriteableFileExtensionsToList() {
        writableFileExtensionList.add(0, ".bmp");
        writableFileExtensionList.add(1, ".jpg");
        writableFileExtensionList.add(2, ".png");
        writableFileExtensionList.add(3, ".tga");
        writableFileExtensionList.add(4, ".tiff");

    }

    public Image getCurrentImage() {
        return imageList.get(currentFileIndex);
    }

    public Path getCurrentImagePath() {
        return fileNames.get(currentFileIndex);
    }

    public ImageAndPathHolder getPreviousImage() {
        ImageAndPathHolder imageAndPathHolder = new ImageAndPathHolder();
        if(fileNames.size() == 1) {
            imageAndPathHolder.setPath(outOfBoundsImagePath);
            return imageAndPathHolder;
        } else if (-1 == currentFileIndex - 1) {
            imageAndPathHolder.setPath(outOfBoundsImagePath);
            return imageAndPathHolder;
        } else if (fileNames.size() == 0){
            imageAndPathHolder.setPath(outOfBoundsImagePath);
            return imageAndPathHolder;
        } else {
            imageAndPathHolder.setImage(imageList.get(currentFileIndex - 1));
            imageAndPathHolder.setPath(fileNames.get(currentFileIndex - 1));
            currentFileIndex--;
            return imageAndPathHolder;
        }

    }

    public ImageAndPathHolder getNextImage() {
        ImageAndPathHolder imageAndPathHolder = new ImageAndPathHolder();
        if(fileNames.size() == 1) {
            imageAndPathHolder.setPath(outOfBoundsImagePath);
            return imageAndPathHolder;

        } else if (fileNames.size() == currentFileIndex + 1) {
            imageAndPathHolder.setPath(outOfBoundsImagePath);
            return imageAndPathHolder;

        } else if (fileNames.size() == 0) {
            imageAndPathHolder.setPath(outOfBoundsImagePath);
            return imageAndPathHolder;

        } else {
            imageAndPathHolder.setImage(imageList.get(currentFileIndex + 1));
            imageAndPathHolder.setPath(fileNames.get(currentFileIndex + 1));
            currentFileIndex++;
            return imageAndPathHolder;
        }
    }

    public void setCurrentImageIndex(Path imagePath) {
        for(int i = 0; i < fileNames.size(); i++) {

            if (imagePath.equals(fileNames.get(i))) {
                currentFileIndex = i;
            }
        }
    }

    private boolean fileIsAPhoto(Path photoPath) {
        String extension = FilenameUtils.getExtension(photoPath.toString()).toLowerCase(Locale.ROOT);
        for(String fileExtensionName : readableExtensionList) {
            if (fileExtensionName.contains(extension)) {
                return true;
            }
        }
        return false;
    }

    public String getPhotoExtension() {
        return FilenameUtils.getExtension(getCurrentImage().toString());
    }

    private Image loadGIFRespectingImage(Path imagePath) {
        Image toRtn;
        if(FilenameUtils.getExtension(imagePath.toString()).equals("gif")) {
            toRtn = new Image(imagePath.toUri().toString());
        } else {
            try {
                toRtn = SwingFXUtils.toFXImage(ImageIO.read(imagePath.toFile()), null);
            } catch (IOException e) {
                e.printStackTrace();
                toRtn = null;
            }
        }
        return toRtn;
    }

    public Image loadImage() {
        Image image;
        try {
            File firstImagePath = this.getCurrentImagePath().toFile();

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

    public Image loadImageFromPath(Path imagePath) {
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

    public List<Path> getListOfFilePaths() {
        return fileNames;
    }

    public String getDirectoryAsString() {
        return getCurrentImagePath().getParent().toString();
    }

    public List<String> getWritableFileExtensionList() {
        return writableFileExtensionList;
    }
}