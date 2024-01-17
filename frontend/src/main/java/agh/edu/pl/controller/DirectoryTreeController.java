package agh.edu.pl.controller;

import agh.edu.pl.app.FrontendApp;
import agh.edu.pl.model.ThumbnailGallery;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.substring;

public class DirectoryTreeController {
    private static final String IMAGE_API_ADDRESS = "http://localhost:8080/images";
    public static final String rootFolder = "All";
    public static SimpleStringProperty currentPath = new SimpleStringProperty(rootFolder);

    public void updateFolders(TreeView<String> directoryTree) throws URISyntaxException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FrontendApp.class.getClassLoader().getResource("views/directoryTreeController.fxml"));
        TreeItem<String> rootItem = new TreeItem<>(rootFolder);
        Gson gson = new Gson();
        URI uri = new URI(IMAGE_API_ADDRESS + "/getAllFolders");
        String responseString = getHTTP(uri);

        List<String> jsonStringList = gson.fromJson(responseString, new TypeToken<List<String>>() {}.getType());

        for (String jsonString : jsonStringList) {
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            String path = jsonObject.get("path").getAsString();
            if(!rootFolder.equals(path))
                addPathToTree(rootItem, path.replace("\"", ""));
        }

        directoryTree.setRoot(rootItem);

        directoryTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.currentPath.setValue(getFullPath(newValue));
            }
        });
    }
    private String getHTTP(URI uri) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(uri);
        HttpResponse response = httpClient.execute(req);
        return new BasicResponseHandler().handleResponse(response);
    }

    public void addPathToTree(TreeItem<String> root, String path) {
        if (path.equals(currentPath.getValue()))
            return;
        String[] parts = path.split("/");
        TreeItem<String> current = root;

        for (String part : parts) {
                if (part.equals(rootFolder)) continue;
                TreeItem<String> child = findChild(current, part);
                if (child == null) {
                    child = new TreeItem<>(part);
                    current.getChildren().add(child);
                }
                current = child;
        }
    }

    private TreeItem<String> findChild(TreeItem<String> parent, String value) {
        for (TreeItem<String> child : parent.getChildren()) {
            if (child.getValue().equals(value)) {
                return child;
            }
        }
        return null;
    }
    private String getFullPath(TreeItem<String> item) {
        StringBuilder pathBuilder = new StringBuilder(item.getValue());
        TreeItem<String> parent = item.getParent();
        while (parent != null) {
            pathBuilder.insert(0, parent.getValue() + "/");
            parent = parent.getParent();
        }
        return pathBuilder.toString();
    }
}
