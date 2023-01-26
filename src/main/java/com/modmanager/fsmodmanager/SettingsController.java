package com.modmanager.fsmodmanager;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsController {
    public Button setupButton;

    public void setupClicked(ActionEvent actionEvent) {

        try {
            new File(MainPageController.getGameDirectory().getPath() + "/fs_mod_manager/config.properties").createNewFile();
            new File(MainPageController.getGameDirectory().getPath() + "/fs_mod_manager").mkdirs();
            new File(MainPageController.getGameDirectory().getPath() + "/mods_inactive").mkdirs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Success");

        Properties properties = new Properties();

        try {
            FileInputStream inFile = new FileInputStream(MainPageController.getGameDirectory().getPath() + "/fs_mod_manager/config.properties");
            FileOutputStream outFile = new FileOutputStream(MainPageController.getGameDirectory().getPath() + "/fs_mod_manager/config.properties");

            properties.load(inFile);
            inFile.close();

            properties.setProperty("gameDirectory", MainPageController.getGameDirectory().getPath());
            properties.store(outFile, null);


            outFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
