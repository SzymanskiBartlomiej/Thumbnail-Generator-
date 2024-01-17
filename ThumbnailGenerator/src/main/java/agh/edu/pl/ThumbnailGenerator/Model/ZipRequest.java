package agh.edu.pl.ThumbnailGenerator.Model;

import agh.edu.pl.ThumbnailGenerator.Enums.ImageSize;

public class ZipRequest {
    private final String path;
    private final ImageSize imageSize;

    public ZipRequest(String path, ImageSize imageSize) {
        this.path = path;
        this.imageSize = imageSize;
    }

    public String getPath() {
        return path;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }
}
