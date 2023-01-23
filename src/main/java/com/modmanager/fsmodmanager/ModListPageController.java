package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;


public class ModListPageController {

    public ListView modListView;
    public Button refreshButton;

    public File modsFolder;
    public Label countLabel;

    public void initialize(){
        refresh();
    }

    public void refreshPressed(ActionEvent actionEvent) {
        modListView.getItems().clear();
        refresh();
    }

    /**
     * refresh reads all mods in the modsfolder and count them
     */
    public void refresh (){

        modsFolder = new File(MainPageController.getGameDirectory() + "\\mods");
        File[] mods = modsFolder.listFiles();

        for (File f : mods) {
            modListView.getItems().add(f.getName());
        }
        countLabel.setText(String.valueOf(modsFolder.listFiles().length));
    }
}
