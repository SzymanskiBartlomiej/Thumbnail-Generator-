package agh.edu.pl.model;

import agh.edu.pl.enums.ImageSize;
import agh.edu.pl.enums.Status;
import agh.edu.pl.util.PictureLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Thumbnail {
    private String id;

    private Image thumbnailData;
    private final ObjectProperty<Status> status;
    private final ObjectProperty<Image> displayedThumbnail;
    private final ImageSize size;
    private final Picture originalPicture;


    public Thumbnail(Picture picture, Status status, ImageSize size) {
        this.displayedThumbnail = new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(picture.getByteData())));
        this.thumbnailData = picture.getPictureData();
        this.status = new SimpleObjectProperty<>(status);
        this.originalPicture = picture;
        this.size = size;
        changeStatus(Status.IN_PROGRESS);
    }

    public void changeStatus(Status status) {
        this.status.set(status);
        switch (status) {
            case IN_PROGRESS -> displayedThumbnail.set(PictureLoader.processingImage);
            case FAILED -> displayedThumbnail.set(PictureLoader.failedImage);
            case PROCESSED -> displayedThumbnail.set(thumbnailData);
        }
    }



    public ObjectProperty<Image> displayedThumbnailProperty() {
        return displayedThumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return originalPicture.getName();
    }

    public void setThumbnailData(Image thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public String getExtension() {
        return originalPicture.getExtension();
    }

    public byte[] getOriginalData() {
        return originalPicture.getByteData();
    }

    public ImageSize getSize() {
        return size;
    }

    public String getOriginalId(){
        return  originalPicture.getId();
    }
}
