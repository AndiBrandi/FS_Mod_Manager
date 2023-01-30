package com.modmanager.fsmodmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class MainPageController {


    public DirectoryChooser directoryChooser = new DirectoryChooser();
    public ListView<String> modPackLV;
    public ListView<Mod> modsLV;
    public TextField gameDirTextField;
    public TextField searchField;
    public Button modListButton;
    public Button settingsButton;
    public Button browseDirButton;
    public Button addProfileButton;
    public Button loadProfilesButton;

//    private static String gameDirectoryPath;
    public static File gameDirectory;
    public static File activeModsFolder;
    public static File inactiveModsFolder;
    public static File profilesXmlFile;
    public static HashMap<String, ArrayList<Mod>> modpacks = new HashMap<>();
    public Button loadProfileButton;

    public void initialize() {

        try {
            Properties prop = new Properties();
            FileInputStream inFile = new FileInputStream("C:\\Users\\andib.DESKTOP-I3IARB9\\Documents\\My Games\\FarmingSimulator2022\\fs_mod_manager\\config.properties");
            prop.load(inFile);
            gameDirTextField.setText(prop.getProperty("gameDirectory"));
            setGameDirectory(new File(prop.getProperty("gameDirectory")));

            gameDirectory = new File(gameDirectory.getPath());
            activeModsFolder = new File(gameDirectory.getPath() + "\\mods");
            inactiveModsFolder = new File(gameDirectory.getPath() + "\\mods_inactive");
            profilesXmlFile = new File(gameDirectory.getPath() + "\\fs_mod_manager\\profiles.xml");


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
        setGameDirectory(selectedDir);

    }

    public void newProfileClicked(ActionEvent actionEvent) {

        uiLoader("newProfilePage.fxml","New Profile");

    }

    public void loadProfileIntoGame(ActionEvent actionEvent) {

        ModPackManager.loadProfile(modPackLV.getSelectionModel().getSelectedItem());          //just for testing

    }

    public void loadProfilesFromDisk(ActionEvent actionEvent) {

        ObservableList modList = FXCollections.observableArrayList();
//        File modsFolder = new File(MainPageController.getGameDirectory() + "\\mods");
//        File[] mods = modsFolder.listFiles();
//        ArrayList<File> array = new ArrayList();
//
//        for (File f : mods) {
//            array.add(f);
//        }


//        profile.createNewProfile(array, "Flo");
        modpacks = ModPackManager.getModpacksFromXML(new File(MainPageController.getGameDirectory().getPath() + "\\fs_mod_manager\\profiles.xml"));

        for (String key : modpacks.keySet()) {
            modList.add(key);
        }

        modPackLV.setItems(modList);

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


//    public static String getGameDirectoryPath() {
//        return gameDirectoryPath;
//    }


    public static void setGameDirectory(File gameDirectory) {
        MainPageController.gameDirectory = gameDirectory;
    }

    public static HashMap<String, ArrayList<Mod>> getModpacks() {
        return modpacks;
    }

    public static File getGameDirectory() {
        return gameDirectory;
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
