package com.modmanager.fsmodmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Stage mainStage;
    public static String stylesheet = String.valueOf(Main.class.getResource("stylesheets/darker_stylesheet.css"));


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Main.getStylesheet());
        stage.setTitle("FS Mod Manager");
        stage.setScene(scene);
        stage.show();

        mainStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }


    public static String getStylesheet() {
        return stylesheet;
    }
}
