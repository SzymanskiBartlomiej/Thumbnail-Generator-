package agh.edu.pl.enums;

public enum ImageSize {
    SMALL,
    MEDIUM,
    LARGE;

    public int getThumbnailSize() {
       return switch (this) {
            case SMALL -> 50;
            case MEDIUM -> 100;
            case LARGE -> 150;
        };
    }
}
