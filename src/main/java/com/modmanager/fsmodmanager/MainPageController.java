package com.modmanager.fsmodmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class MainPageController {


    public ListView modpackList;
    public Button modListButton;
    public Button settingsButton;
    public DirectoryChooser directoryChooser = new DirectoryChooser();
    public Button browseDirButton;
    public TextField gameDirTextField;

    public static String gameDirectory;
    public TextField searchField;
    public Button addProfileButton;

    public void initialize() {

        try {
            Properties prop = new Properties();
            FileInputStream inFile = new FileInputStream("C:\\Users\\andib.DESKTOP-I3IARB9\\Documents\\My Games\\FarmingSimulator2022\\fs_mod_manager\\config.properties");
            prop.load(inFile);
            gameDirTextField.setText(prop.getProperty("gameDirectory"));
            setGameDirectory(prop.getProperty("gameDirectory"));

        } catch (Exception e) {

        }

    }

    public void modlistButtonPressed(ActionEvent actionEvent) {


        uiLoader("modListPage.fxml", "Modliste");

    }

    public void settingsButtonPressed(ActionEvent actionEvent) {

        uiLoader("settingsPage.fxml", "Settings");
    }

    public void browseClicked(ActionEvent actionEvent) {
        File selectedDir = directoryChooser.showDialog(Main.mainStage);
        gameDirTextField.setText(selectedDir.getAbsolutePath());
        gameDirectory = gameDirTextField.getText();

    }

    public void addProfileButtonClicked(ActionEvent actionEvent) {



    }

    /**
     * Öffnet FXML Seiten nach ihrem Namen mit jeweiligem Titel
     *
     * @param resource
     * @param title
     */
    private void uiLoader(String resource, String title) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(resource));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.getStylesheet());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String getGameDirectory() {
        return gameDirectory;
    }

    public static void setGameDirectory(String gameDirectory) {
        MainPageController.gameDirectory = gameDirectory;
    }
}
