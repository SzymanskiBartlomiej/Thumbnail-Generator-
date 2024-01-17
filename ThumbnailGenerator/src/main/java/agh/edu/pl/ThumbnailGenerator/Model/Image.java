package agh.edu.pl.ThumbnailGenerator.Model;

 import agh.edu.pl.ThumbnailGenerator.Enums.ImageSize;
import org.springframework.data.annotation.Id;

import java.util.Optional;

public class Image {

    @Id
    private String id;
    private String name;
    private String extension;
    private byte[] data;
    private String thumbnailIdSmall;
    private String thumbnailIdMedium;
    private String thumbnailIdLarge;
    private String path;

    public Image(String name, String extension, byte[] data, String path) {
        this.name = name;
        this.extension = extension;
        this.data = data;
        this.path = path;
    }

    public Image() {
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setData(byte[] data){
        this.data = data;
    }

    public String getThumbnailIdSmall() {
        return thumbnailIdSmall;
    }

    public void setThumbnailIdSmall(String thumbnailIdSmall) {
        this.thumbnailIdSmall = thumbnailIdSmall;
    }

    public String getThumbnailIdMedium() {
        return thumbnailIdMedium;
    }

    public void setThumbnailIdMedium(String thumbnailIdMedium) {
        this.thumbnailIdMedium = thumbnailIdMedium;
    }

    public String getThumbnailIdLarge() {
        return thumbnailIdLarge;
    }

    public void setThumbnailIdLarge(String thumbnailIdLarge) {
        this.thumbnailIdLarge = thumbnailIdLarge;
    }

    public Optional<String> checkIfThumbnailExistsBySize(ImageSize imageSize){
        String thumbnailId = switch (imageSize) {
            case SMALL -> getThumbnailIdSmall();
            case MEDIUM -> getThumbnailIdMedium();
            case LARGE -> getThumbnailIdLarge();
        };
        if (thumbnailId != null) {
            return Optional.of(thumbnailId);
        } else {
            return Optional.empty();
        }
    }

    public void setThumbnailBySize(String thumbnailId, ImageSize imageSize){
        switch (imageSize) {
            case SMALL -> setThumbnailIdSmall(thumbnailId);
            case MEDIUM -> setThumbnailIdMedium(thumbnailId);
            case LARGE -> setThumbnailIdLarge(thumbnailId);
        }
    }

    public String getPath() {
        return path;
    }
}
