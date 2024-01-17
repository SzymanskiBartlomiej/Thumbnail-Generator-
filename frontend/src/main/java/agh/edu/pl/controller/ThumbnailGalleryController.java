package agh.edu.pl.controller;

import agh.edu.pl.enums.ImageSize;
import agh.edu.pl.model.Picture;
import agh.edu.pl.model.ThumbnailGallery;
import agh.edu.pl.util.PictureLoader;
import agh.edu.pl.util.ThumbnailDownloader;
import io.reactivex.rxjava3.core.Observable;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public class ThumbnailGalleryController {

    public ChoiceBox<String> sizeChoiceBox;
    public Button uploadButton;
    public TilePane imagesListView;
    private ThumbnailGallery gallery;
    private AppController appController;
    @FXML
    private TreeView<String> directoryTree;
    private DirectoryTreeController directoryTreeController = new DirectoryTreeController();

    @FXML
    public void initialize() {
        try {
            directoryTreeController.updateFolders(directoryTree);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadClicked(ActionEvent actionEvent) {
        PictureLoader loader = new PictureLoader();
        List<Picture> pictures = loader.loadPicturesPopup();
        for (Picture p : pictures) {
            gallery.addPicture(p);
            directoryTreeController.addPathToTree(directoryTree.getRoot(), p.getPath());
        }
    }

    public void addPicture(Picture picture) {

        gallery.addPicture(picture);
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void setModel(ThumbnailGallery gallery) {
        this.gallery = gallery;
        gallery.getDisplayedSize().bind(sizeChoiceBox.valueProperty());
        gallery.getPictures().addListener(new ListChangeListener<Picture>() {
            @Override
            public void onChanged(Change<? extends Picture> c) {
                var currentPath = directoryTreeController.currentPath.getValue();
                while (c.next()) {
                    for (Picture added : c.getAddedSubList()) {
                        if (added.getPath().equals(currentPath))
                            initializePicture(added);
                    }
                }
            }
        });

        directoryTreeController.currentPath.addListener((observable, oldValue, newValue) -> {
            imagesListView.getChildren().clear();
            for (var picture : gallery.getPictures()){
                if (picture.getPath().equals(newValue))
                    initializePicture(picture);
            }
        });
        try {
            new ThumbnailDownloader().getAllPictures().observeOn(JavaFxScheduler.platform()).subscribe(this::addPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializePicture(Picture added) {
        added.updateDisplayedThumbnail(ImageSize.valueOf(gallery.getDisplayedSize().getValue().toUpperCase()));
        ImageView image = new ImageView(added.displayedThumbnailProperty().getValue());
        Platform.runLater(() -> {
            imagesListView.getChildren().add(image);
        });
        image.imageProperty().bind(added.displayedThumbnailProperty());

//        initial thumbnail size
        image.setFitHeight(ImageSize.valueOf(gallery.getDisplayedSize().getValue().toUpperCase()).getThumbnailSize());
        image.setFitWidth(ImageSize.valueOf(gallery.getDisplayedSize().getValue().toUpperCase()).getThumbnailSize());

//        thumbnail size listener
        gallery.getDisplayedSize().addListener((observable, oldSize, newSize) -> {
            image.setFitHeight(ImageSize.valueOf(newSize.toUpperCase()).getThumbnailSize());
            image.setFitWidth(ImageSize.valueOf(newSize.toUpperCase()).getThumbnailSize());
            added.updateDisplayedThumbnail(ImageSize.valueOf(gallery.getDisplayedSize().getValue().toUpperCase()));
        });

//        click listener for view change
        image.setOnMouseClicked(event -> appController.showSingleImageView(added));

        initializeHoverImage(image, added);
    }

    private void initializeHoverImage(ImageView image, Picture added) {
        Stage hoverImageStage = new Stage();
        StackPane hoverImagePane = new StackPane();
        ImageView hoverImageView = new ImageView(added.originalImageProperty().getValue());
        hoverImagePane.getChildren().add(hoverImageView);
        hoverImageStage.initStyle(StageStyle.UNDECORATED);
        hoverImageView.setFitHeight(300);
        hoverImageView.setPreserveRatio(true);
        hoverImageStage.setScene(new Scene(hoverImagePane));
        image.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hoverImageStage.setX(event.getScreenX() + 5);
                hoverImageStage.setY(event.getScreenY() + 5);
            }
        });
        image.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hoverImageStage.show();
            } else {
                hoverImageStage.hide();
            }
        });
    }

    @FXML
    private void addDirectory(ActionEvent event) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Podaj nazwe folderu");
        Button okButton = (Button) textInputDialog.getDialogPane().lookupButton(ButtonType.OK);

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextField inputField = textInputDialog.getEditor();
                var path = directoryTreeController.currentPath.getValue() + "/";
                path += inputField.getText();
                directoryTreeController.addPathToTree(directoryTree.getRoot(), path);
            }
        } );
        textInputDialog.show();
    }
}
