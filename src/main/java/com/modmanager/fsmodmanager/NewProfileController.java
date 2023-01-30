package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewProfileController {
    public Button createProfileButton;
    public ListView<String> modsLV;
    public TextField profileNameTF;

    private ObservableList<String> mods = FXCollections.observableArrayList();

    public void initialize() {

        modsLV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        File[] f1 = MainPageController.getActiveModsFolder().listFiles();
        File[] f2 = MainPageController.getInactiveModsFolder().listFiles();

        for (File f : f1) {
//            mods.add(new Mod(f.getPath()));
            mods.add(f.getName());
        }
        for (File f : f2) {
//            mods.add(new Mod(f.getPath()));
            mods.add(f.getName());
        }
        System.out.println(mods);
        modsLV.setItems(mods);

    }

    public void createProfileClicked(ActionEvent actionEvent) {

        if(!profileNameTF.getText().isEmpty() && profileNameTF.getText().length() >= 3) {
            ArrayList<String> selected = new ArrayList<>();
            selected.addAll(modsLV.getSelectionModel().getSelectedItems());

            ModPackManager.createNewProfile(selected, profileNameTF.getText());
            System.out.println("Successfully created profile: " + profileNameTF.getText());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Profile name cannot be empty!");
            alert.setContentText("Type a name with atlest 3 letters");
            alert.show();
        }
    }
}
