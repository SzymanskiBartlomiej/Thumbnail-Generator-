package agh.edu.pl.model;

import agh.edu.pl.enums.ImageSize;
import agh.edu.pl.enums.Status;
import agh.edu.pl.util.PictureLoader;
import agh.edu.pl.util.ThumbnailDownloader;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Picture {

    private String id;
    private final String name;
    private final String extension;
    private final String path;
    private final ObjectProperty<Image> originalImage;
    private final ObjectProperty<Image> displayedThumbnail;
    private final byte[] byteData;
    private final HashMap<ImageSize, Thumbnail> thumbnails = new HashMap<>();
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public Picture(byte[] byteData, String name, String extension, String path) {
        this.name = name;
        this.extension = extension;
        this.byteData = byteData;
        this.originalImage = new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(byteData)));
        this.displayedThumbnail = new SimpleObjectProperty<>(PictureLoader.processingImage);
        this.path = path;
    }

    public Picture(String id, byte[] byteData, String name, String extension, String path) {
        this(byteData, name, extension, path);
        this.id = id;
    }

    public void updateDisplayedThumbnail(ImageSize size) {
        if (!thumbnails.containsKey(size)) {
            Thumbnail newThumbnail = new Thumbnail(this, Status.IN_PROGRESS, size);
            thumbnails.put(size, newThumbnail);
            Task<Void> thumbnailTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    ThumbnailDownloader thumbnailDownloader = new ThumbnailDownloader();
                    thumbnailDownloader.pollForThumbnail(newThumbnail);
                    return null;
                }
            };

            thumbnailTask.setOnSucceeded(event -> {
                displayedThumbnail.bind(newThumbnail.displayedThumbnailProperty());
            });

            executor.execute(thumbnailTask);
        } else {
            displayedThumbnail.bind(thumbnails.get(size).displayedThumbnailProperty());
        }
    }

    public ObjectProperty<Image> originalImageProperty() {
        return originalImage;
    }

    public ObjectProperty<Image> displayedThumbnailProperty() {
        return displayedThumbnail;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getByteData() {
        return byteData;
    }

    public Image getPictureData() {
        return originalImage.get();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPath() {
        return path;
    }
}
