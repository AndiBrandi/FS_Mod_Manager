package com.modmanager.fsmodmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * Modpack profile Manager that uses Properties files to store profiles
 */
public class ModPackManagerPROP {

    public static void createNewProfile(ArrayList<String> mods, String profileName) {

        try (FileInputStream inputStream = new FileInputStream(MainPageController.getProfilesPropertiesFile());
             FileOutputStream outputStream = new FileOutputStream(MainPageController.getProfilesPropertiesFile())) {

            Properties prop = new Properties();
            StringBuilder sb = new StringBuilder("");
            prop.load(inputStream);

            for (String s : mods) {
                sb.append(sb + ";");
            }
            sb.deleteCharAt(sb.length());

            prop.setProperty(profileName, sb.toString());

            prop.store(outputStream, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void deleteProfile(String profileName) {

        try (FileInputStream inputStream = new FileInputStream(MainPageController.getProfilesPropertiesFile());
             FileOutputStream outputStream = new FileOutputStream(MainPageController.getProfilesPropertiesFile())) {           //Try with resources, closed den Filestream autom. wenn er nicht mehr gebraucht wird.

            Properties prop = new Properties();
            prop.load(inputStream);

            prop.remove(profileName);

            prop.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadProfile(String profileName) {


    }

    /**
     *
     * @param propFile Datei die die profile beinhaltet
     * @return
     */
    public static HashMap<String, ArrayList<Mod>> getModpacksFromProp(File propFile) {

        //Bisschen Code vom ersten Mod manager kopieren
        

        return
    }


}
