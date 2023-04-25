package com.modmanager.fsmodmanager;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.Properties;

public class SettingsController {

    public DirectoryChooser directoryChooser = new DirectoryChooser();
    public Button setupButton;
    public TextField gameDirTextField;
    public Button browseGameDirButton;

    public void initialize() {

        try (FileInputStream in = new FileInputStream(System.getProperty("user.home") + "\\Documents\\My Games\\FarmingSimulator2022\\fs_mod_manager\\config.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            gameDirTextField.setText(prop.getProperty("gameDirectory"));
        } catch (IOException e) {
//            e.printStackTrace();
//            gameDirTextField.setText(System.getProperty("user.home") + "\\Documents\\My Games\\FarmingSimulator2022\\");
        }

    }

    public void setupClicked(ActionEvent actionEvent) {

        if (gameDirTextField.getText().isEmpty()) {
            CustomAlerts.errorAlert("Game not found","Please enter your FS22 game installation path");
            return;
        }
        MainPageController.gameDirectoryFolder = new File(gameDirTextField.getText());
        MainPageController.refreshDirsAndFiles();
        Properties properties = new Properties();

        try {
            new File(MainPageController.getGameDirectoryFolder().getPath() + "\\fs_mod_manager").mkdirs();
            new File(MainPageController.getGameDirectoryFolder().getPath() + "\\mods_inactive").mkdirs();
            new File(MainPageController.getGameDirectoryFolder().getPath() + "\\fs_mod_manager\\config.properties").createNewFile();
            new File(MainPageController.getGameDirectoryFolder().getPath() + "\\fs_mod_manager\\profiles.xml").createNewFile();


            properties.load(new FileReader(MainPageController.getGameDirectoryFolder().getPath() + "\\fs_mod_manager\\config.properties"));
            properties.setProperty("gameDirectory", MainPageController.getGameDirectoryFolder().getPath());
            properties.store(new FileWriter(MainPageController.getGameDirectoryFolder().getPath() + "\\fs_mod_manager\\config.properties"), null);


        } catch (IOException e) {
            e.printStackTrace();
        }

        MainPageController.refreshDirsAndFiles();
    }


    public void browseClicked(ActionEvent actionEvent) {

        File selectedDir = directoryChooser.showDialog(Main.mainStage);
        gameDirTextField.setText(selectedDir.getAbsolutePath());
        MainPageController.gameDirectoryFolder = selectedDir;

    }


}
