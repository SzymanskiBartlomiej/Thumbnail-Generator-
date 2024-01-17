package agh.edu.pl.ThumbnailGenerator.Model;

public class ImageRequest {
    private final String name;
    private final String extension;
    private final String data;
    private final String path;

    public ImageRequest(String name, String extension, String data, String path) {
        this.name = name;
        this.extension = extension;
        this.data = data;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getData() {
        return data;
    }

    public String getPath() {
        return path;
    }
}
