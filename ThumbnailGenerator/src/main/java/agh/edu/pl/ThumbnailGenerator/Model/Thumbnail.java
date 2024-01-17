package agh.edu.pl.ThumbnailGenerator.Model;

import agh.edu.pl.ThumbnailGenerator.Enums.ImageSize;
import agh.edu.pl.ThumbnailGenerator.Enums.Status;

public class Thumbnail extends Image{
    private ImageSize size;
    private Status status;

    public Thumbnail(String name, String extension, byte[] data, ImageSize size, Status status, String path) {
        super(name, extension, data, path);
        this.size = size;
        this.status = status;
    }

    public Thumbnail(Image img, ImageSize size, Status status) {
        this(img.getName(), img.getExtension(), img.getData(), size, status, img.getPath());
    }

    public Thumbnail() {
    }

    public ImageSize getSize() {
        return size;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
