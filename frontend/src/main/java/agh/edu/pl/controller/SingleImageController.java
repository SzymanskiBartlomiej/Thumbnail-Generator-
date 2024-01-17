package agh.edu.pl.controller;

import agh.edu.pl.model.Picture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class SingleImageController {
    public Button returnButton;
    public Label nameLabel;
    public ImageView imageView;
    private AppController appController;

    @FXML
    public void initialize() {
    }

    public void returnButtonClicked(ActionEvent actionEvent) {
        appController.showGalleryView();
    }

    public void setPicture(Picture picture) {
        this.imageView.imageProperty().set(picture.getPictureData());
        this.nameLabel.setText(picture.getName());
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
