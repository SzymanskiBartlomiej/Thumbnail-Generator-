package agh.edu.pl.ThumbnailGenerator.Enums;

public enum ImageSize {
    SMALL,
    MEDIUM,
    LARGE;
    public int getThumbnailSize() {
        int thumbnailSize;
        switch (this) {
            case SMALL -> thumbnailSize = 50;
            case MEDIUM -> thumbnailSize = 100;
            case LARGE -> thumbnailSize = 150;
            default -> thumbnailSize = 0;
        }
        return thumbnailSize;
    }
}
