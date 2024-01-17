package agh.edu.pl.ThumbnailGenerator.Model;

import agh.edu.pl.ThumbnailGenerator.Enums.ImageSize;

public class ThumbnailRequest {
    private final String imageId;
    private final ImageSize imageSize;

    public ThumbnailRequest(String imageId, ImageSize imageSize) {
        this.imageId = imageId;
        this.imageSize = imageSize;
    }

    public String getImageId() {
        return imageId;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }
}
