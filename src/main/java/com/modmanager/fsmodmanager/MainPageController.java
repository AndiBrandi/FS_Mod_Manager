package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class MainPageController {


    public ListView modpackList;
    public Button modListButton;
    public Button settingsButton;
    public ToggleButton toggleButton;
    public DirectoryChooser gameDir = new DirectoryChooser();
    public Button browseDirButton;
    public TextField gameDirTextField;
    public File modsFolder;

    public void initialize() {

    }

    public void modlistButtonPressed(ActionEvent actionEvent) {
        System.out.println("LLLLLLLL");
        ObservableList<File> modsList = FXCollections.observableArrayList();
        modsFolder = new File(gameDirTextField.getText() + "\\mods");
        System.out.println(modsFolder);
        modsList.addAll(modsFolder.listFiles());


    }

    public void settingsButtonPressed(ActionEvent actionEvent) {
    }

    public void browseClicked(ActionEvent actionEvent) {
        File selectedDir = gameDir.showDialog(Main.mainStage);
        gameDirTextField.setText(selectedDir.getAbsolutePath());

    }

}
