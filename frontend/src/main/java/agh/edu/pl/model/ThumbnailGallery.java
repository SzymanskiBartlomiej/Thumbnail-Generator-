package agh.edu.pl.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ThumbnailGallery {
    private final ObservableList<Picture> pictures = FXCollections.observableArrayList();
    private StringProperty displayedSize;

    public ThumbnailGallery() {
        displayedSize = new SimpleStringProperty("small");
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    public ObservableList<Picture> getPictures() {
        return pictures;
    }

    public StringProperty getDisplayedSize() {
        return displayedSize;
    }

    public void clear() {
        pictures.clear();
    }
}
