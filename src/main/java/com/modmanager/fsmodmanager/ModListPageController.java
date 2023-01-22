package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.Arrays;

public class ModListPageController {
    public ListView modListView;
    public Button refreshButton;

    public File modsFolder;
    public Label countLabel;

    public void refreshPressed(ActionEvent actionEvent) {

        int count = 0;
        ObservableList<File> modsList = FXCollections.observableArrayList();
        modsFolder = new File(MainPageController.getGameDirectory() + "\\mods");
        File[] mods = modsFolder.listFiles();
        
        for (File f : mods) {
            ++count;
            modListView.getItems().add(f.getName());

        }
        countLabel.setText("Total count: "+ count);

    }
}
