package agh.edu.pl.util;

import agh.edu.pl.controller.DirectoryTreeController;
import agh.edu.pl.model.Picture;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PictureLoader {

    private final String[] ALLOWED_EXTENSIONS = {"jpg", "png"};
    private final String[] ARCHIVE_EXTENSIONS = {"zip"};
    public static final Image processingImage = loadImage("src/main/resources/images/processing.gif");
    public static final Image failedImage = loadImage("src/main/resources/images/failed.png");

    public List<Picture> loadPicturesPopup() {
        ThumbnailDownloader thumbnailDownloader = new ThumbnailDownloader();
        Stage popup = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Pictures");
        List<File> files = fileChooser.showOpenMultipleDialog(popup);

        List<Picture> pictures = new ArrayList<>();
        for (File f : files) {
            String fileName = f.getName();
            String extension = getExtensionFromString(fileName);
            if (Arrays.asList(ALLOWED_EXTENSIONS).contains(extension)) {
                try {
                    Picture newPicture = new Picture(Files.readAllBytes((f.toPath())), fileName, extension, DirectoryTreeController.currentPath.getValue());
                    thumbnailDownloader.postImage(newPicture);
                    pictures.add(newPicture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Arrays.asList(ARCHIVE_EXTENSIONS).contains(extension)) {
                try {
                    byte[] zipBytes = Files.readAllBytes(f.toPath());
                    UnzipZip unzipZip = new UnzipZip(zipBytes);
                    List<Picture> zipPictures = unzipZip.readZip();
                    for (Picture p : zipPictures){
                        thumbnailDownloader.postImage(p);
                    }
                    pictures.addAll(zipPictures);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pictures;
    }


    private static Image loadImage(String url) {
        File fi = new File(url);
        byte[] fileContent = {};
        try {
            fileContent = Files.readAllBytes(fi.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Image(new ByteArrayInputStream(fileContent));
    }



    public String getExtensionFromString(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
