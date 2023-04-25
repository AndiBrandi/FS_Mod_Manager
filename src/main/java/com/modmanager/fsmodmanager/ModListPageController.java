package com.modmanager.fsmodmanager;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;


public class ModListPageController {

    public ListView<String> modListView;
    public ListView<String> inactiveModListView;
    public Button refreshButton;
    public Label countLabel;
    public Label inactiveCountLabel;

    public File modsFolder;
    public File inactiveModsFolder;

    public void initialize() {
        refresh();
    }


    /**
     * refresh reads all mods in the modsfolder and count them
     */
    public void refresh() {

        modListView.getItems().clear();
        inactiveModListView.getItems().clear();

        modsFolder = new File(MainPageController.getGameDirectoryFolder().getPath() + "\\mods");
        inactiveModsFolder = new File(MainPageController.getGameDirectoryFolder().getPath() + "\\mods_inactive");
        File[] mods = modsFolder.listFiles();
        File[] inactiveMods = inactiveModsFolder.listFiles();

        for (File f : mods) {
            modListView.getItems().add(f.getName() + " Version: " + VersionReader.getVersion(f.getPath()));
        }
        for (File f : inactiveMods) {
            inactiveModListView.getItems().add(f.getName() + " Version: " + VersionReader.getVersion(f.getPath()));
        }
        countLabel.setText(String.valueOf(modsFolder.listFiles().length));
        inactiveCountLabel.setText(String.valueOf(inactiveModsFolder.listFiles().length));
    }
}
