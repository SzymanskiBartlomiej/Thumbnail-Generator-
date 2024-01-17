package agh.edu.pl.util;

public class ImageRequestJSON {
    private final String name;
    private final String extension;
    private final String imageSize;
    private final String data;
    private final String id;
    private final String path;

    public ImageRequestJSON(String name, String extension, String imageSize, String data, String id, String path) {
        this.name = name;
        this.extension = extension;
        this.imageSize = imageSize;
        this.data = data;
        this.id = id;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getImageSize() {
        return imageSize;
    }

    public String getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
