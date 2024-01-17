package agh.edu.pl.app;

import agh.edu.pl.controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;


public class FrontendApp extends Application {

    private Stage primaryStage;
    private AppController appController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Thumbnail gallery");
        this.appController = new AppController(primaryStage);
        this.appController.initRootLayout();
    }
}



