package agh.edu.pl.util;

public class ThumbnailRequestJSON {
    private final String imageId;
    private final String imageSize;

    public ThumbnailRequestJSON(String id, String imageSize) {
        this.imageId = id;
        this.imageSize = imageSize;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageSize() {
        return imageSize;
    }
}
