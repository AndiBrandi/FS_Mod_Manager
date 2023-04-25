package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * <p>
 * GameDirectoryTextFeld ---> Settings Page
 * Funktion: Beim Startup schauen ob dateien & ordner schon vorhanden sind
 * <p>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */


public class MainPageController {

    public AnchorPane anchorPane;
    public ListView<String> modPackLV;
    public ListView<Mod> modsLV;
    public Button modListButton;
    public Button settingsButton;
    public Button addProfileButton;
    public Button loadProfilesButton;
    public Button loadProfileButton;
    public Label activeModpackLabel;

    public MenuItem deleteProfileItem;

    public static File gameDirectoryFolder;
    public static File activeModsFolder;
    public static File inactiveModsFolder;
    public static File profilesXmlFile;
    public static HashMap<String, ArrayList<Mod>> modpacks = new HashMap<>();

    public void initialize() {
        try {
            FileStatus status = checkForExistingConfig();
            if (status == FileStatus.GAME_NOT_FOUND) {
                CustomAlerts.errorAlert("Missing Game", "Couldn't find your Farming Simulator Installation. Please configure it manually in the settings");
                return;
            } else if (status == FileStatus.CONFIG_NOT_FOUND) {
                CustomAlerts.errorAlert("Missing config files", "Missing Mod Manager files, use the setup in the settings.");

            } else {

                refreshDirsAndFiles();
            }


        } catch (Exception e) {

        }


        loadProfilesFromDisk();

        checkActiveModpack();


    }


    public void modlistButtonPressed(ActionEvent actionEvent) {

        uiLoader("modListPage.fxml", "Modliste");

    }

    public void settingsButtonPressed(ActionEvent actionEvent) {

        uiLoader("settingsPage.fxml", "Settings");
    }


    public void newProfileClicked(ActionEvent actionEvent) {

        uiLoader("newProfilePage.fxml", "New Profile");

    }

    public void loadProfileIntoGame(ActionEvent actionEvent) {

        ModPackManagerXML.loadProfile(modPackLV.getSelectionModel().getSelectedItem());

        checkActiveModpack();
    }

    public void loadProfilesFromDisk() {

        ObservableList modList = FXCollections.observableArrayList(); // temp variable weil man aus einer Hashmap nicht direkt die profile in die Listview laden kann
        // direkt die profile in die Listview laden kann.
        try {
            modpacks = ModPackManagerXML.getModpacksFromXML(new File(MainPageController.getGameDirectoryFolder().getPath() + "\\fs_mod_manager\\profiles.xml"));

        } catch (Exception e) {

        }

        for (String key : modpacks.keySet()) {
            modList.add(key);
        }

        modPackLV.setItems(modList);

        System.out.println(System.getProperty("user.home") + "\\Documents\\My Games\\FarmingSimulator2022");

    }

    public static void refreshDirsAndFiles() {
        try {
            // NICHT FINAL: der pfad muss aus einer datei gelesen werden können, die keine vorherigen einstellungen braucht und selbst out of the box vom programm bei der installation erstellt wird.
            Properties prop = new Properties();
            prop.load(new FileInputStream(System.getProperty("user.home") + "\\Documents\\My Games\\FarmingSimulator2022\\fs_mod_manager\\config.properties"));

            gameDirectoryFolder = new File(prop.getProperty("gameDirectory"));
            activeModsFolder = new File(gameDirectoryFolder.getPath() + "\\mods");
            inactiveModsFolder = new File(gameDirectoryFolder.getPath() + "\\mods_inactive");
            profilesXmlFile = new File(gameDirectoryFolder.getPath() + "\\fs_mod_manager\\profiles.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void modPackSelected(MouseEvent mouseEvent) {

        ObservableList<Mod> temp = FXCollections.observableArrayList();
        String selected = modPackLV.getSelectionModel().getSelectedItem();

        ArrayList<Mod> mods = modpacks.get(selected);
        for (Mod m : mods) {
            temp.add(m);
        }
        modsLV.setItems(temp);

    }

    public void deleteProfileItemClicked(ActionEvent actionEvent) {

        String selected = modPackLV.getSelectionModel().getSelectedItem();
        ModPackManagerXML.deleteProfile(selected);
        System.out.println("successfully deleted: " + selected);

        loadProfilesFromDisk();

    }

    /**
     * checks which modpack is currently active and displays it on the main page
     * (WIP)
     */
    public void checkActiveModpack() {

        try {

            File[] mods = getActiveModsFolder().listFiles();
            HashMap<String, ArrayList<Mod>> map = getModpacks();
            int activeModCount, modsInProfileCount = 0, counter = 0;
            activeModCount = mods.length;

            //durchläuft alle profile
            for (String s : map.keySet()) {
                System.out.println("checking modpack: " + s);

                //durchläuft alle mods in einem profil
                ArrayList<Mod> list = map.get(s);
                for (File f : mods) {
                    Mod m = new Mod(f.getAbsolutePath());
                    if (list.toString().contains(m.toString())) {
                        System.out.println("Mods folder does contain: " + f);
                        //when active mods folder contains
                        ++counter;

                    } else {

                        System.out.println("Mods folder does NOT contain: " + f);
                    }
                    ++modsInProfileCount;
                }

                if (counter == activeModCount && !(modsInProfileCount <= counter)) {
                    activeModpackLabel.setText(s);
                    counter = 0;
                }

            }

        } catch (Exception e) {

        }

    }


    public FileStatus checkForExistingConfig() throws IOException {

        File gameDir = null;

        try {
            //When game folder cannot be found, return false
            gameDir = new File(System.getProperty("user.home") + "\\Documents\\My Games\\FarmingSimulator2022");
        } catch (Exception e) {

        }

        File configFile = new File(gameDir + "\\fs_mod_manager\\config.properties");
        if (!gameDir.exists()) {                                                                         //if the Farming Simulator Game directory is non-existent
            return FileStatus.GAME_NOT_FOUND;

        } else if (!configFile.exists()) {       //if game Directory is existent but config files are not

            configFile.createNewFile();
            Properties prop = new Properties();
            prop.setProperty("gameDirectory", gameDir.getPath());
            prop.store(new FileWriter(configFile), null);
            return FileStatus.CONFIG_NOT_FOUND;
        } else {
            System.out.println("Found Game and config files");
        }
        //When everything was found return true
        return FileStatus.EVERYTHING_FOUND;
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

    // public static String getGameDirectoryPath() {
    // return gameDirectoryPath;
    // }


    public static HashMap<String, ArrayList<Mod>> getModpacks() {
        return modpacks;
    }

    public static File getGameDirectoryFolder() {
        return gameDirectoryFolder;
    }

    public static File getActiveModsFolder() {
        return activeModsFolder;
    }

    public static File getInactiveModsFolder() {
        return inactiveModsFolder;
    }

    public static File getProfilesXmlFile() {
        return profilesXmlFile;
    }

}
