package com.modmanager.fsmodmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Modpack profile Manager that uses Properties files to store profiles
 * @deprecated XML Modpack Manager System works better.
 */
public class ModPackManagerPROP {

    public static void createNewProfile(ArrayList<String> mods, String profileName) {

        try (FileInputStream inputStream = new FileInputStream(MainPageController.getProfilesXmlFile());
             FileOutputStream outputStream = new FileOutputStream(MainPageController.getProfilesXmlFile())) {

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

        try (FileInputStream inputStream = new FileInputStream(MainPageController.getProfilesXmlFile());
             FileOutputStream outputStream = new FileOutputStream(MainPageController.getProfilesXmlFile())) {           //Try with resources, closed den Filestream autom. wenn er nicht mehr gebraucht wird.

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
     * @param propFile Datei die die profile beinhaltet
     * @return
     */
    public static HashMap<String, ArrayList<Mod>> getModpacksFromProp(File propFile) {

        HashMap<String, ArrayList<Mod>> modpacks = new HashMap<>();

        try {
            String profileName = "";
            FileInputStream in = new FileInputStream(propFile);
            Properties prop = new Properties();
            prop.load(in);
            Set set = prop.keySet();
            Object[] objArr = set.toArray();

            for (int j = 0; j < set.size(); ++j) {
                ArrayList<Mod> mods = new ArrayList<>();                                                    //Arraylist die jeden Moddatei-namen eines Modpacks beinhaltet
                profileName = objArr[j].toString();

                String[] fileNames = prop.getProperty(objArr[j].toString()).split(";");                        //teilt den gesamten string auf in Array mit allen Moddatei-namen

                System.out.println("Modpack " + profileName + "has mods: " + Arrays.toString(fileNames));

                for (String s : fileNames) {
                    mods.add(new Mod(MainPageController.getInactiveModsFolder().getPath() + "\\" + s));         //fügt alle Mods zur Arraylist hinzu
                }
                modpacks.put(profileName, mods);                            //erstellt neuen eintrag in der Hashmap. Key = name des modpacks, Value = Arraylist mit Moddatei-namen


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return modpacks;

    }


}
