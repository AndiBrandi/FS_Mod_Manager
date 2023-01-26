package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;

public class NewProfileController {
    public Button createProfileButton;
    public ListView<File> modsLV;
    public TextField profileNameLabel;

    private ObservableList<File> mods = FXCollections.observableArrayList();

    public void initialize() {
        File[] f1 = MainPageController.getActiveModsFolder().listFiles();
        File[] f2 = MainPageController.getInactiveModsFolder().listFiles();

        for(File f : f1) {
            mods.add(new Mod(f.getPath()));
        }
        for(File f : f2) {
            mods.add(new Mod(f.getPath()));
        }
        System.out.println(mods);
        modsLV.setItems(mods);
    }

    public void createProfileClicked(ActionEvent actionEvent) {



    }
}
