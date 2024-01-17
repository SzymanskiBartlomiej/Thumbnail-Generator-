package agh.edu.pl.controller;

import agh.edu.pl.app.FrontendApp;
import agh.edu.pl.model.Picture;
import agh.edu.pl.model.ThumbnailGallery;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppController {
    private Stage stage;
    private Scene galleryScene;

    public AppController(Stage stage) {
        this.stage = stage;
    }
    public Stage getStage() {
        return stage;
    }
    public void initRootLayout() {
        ThumbnailGallery gallery = new ThumbnailGallery();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FrontendApp.class.getClassLoader().getResource("views/thumbnailsView.fxml"));
        try {
            BorderPane root = loader.load();
            ThumbnailGalleryController controller = (ThumbnailGalleryController) loader.getController();
            controller.setModel(gallery);
            controller.setAppController(this);
            galleryScene = new Scene(root);
            stage.setScene(galleryScene);
            stage.minWidthProperty().bind(root.minWidthProperty());
            stage.minHeightProperty().bind(root.minHeightProperty());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSingleImageView(Picture picture){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FrontendApp.class.getClassLoader().getResource("views/singleImageView.fxml"));
        try {
            BorderPane view = loader.load();
            SingleImageController controller = (SingleImageController) loader.getController();
            controller.setPicture(picture);
            controller.setAppController(this);
            stage.setScene(new Scene(view));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGalleryView(){
        stage.setScene(galleryScene);
    }
}
