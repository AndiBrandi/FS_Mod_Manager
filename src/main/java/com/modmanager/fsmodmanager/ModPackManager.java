package com.modmanager.fsmodmanager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 */
public class ModPackManager {

    static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


    public static void createNewProfile(ArrayList<String> mods, String profileName) {

        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element rootElement = doc.createElement("profiles");
            doc.appendChild(rootElement);


//            doc.createElement("modpack")

            //re-add existing profiles
            NodeList nodes = getElementsFromXML(MainPageController.getProfilesXmlFile());
            for (int j = 0; j <= nodes.getLength() -1; ++j) {
                Element e = doc.createElement("modpack");
                e.setAttribute("name", nodes.item(j).getAttributes().getNamedItem("name").getTextContent());
                e.setTextContent(nodes.item(j).getTextContent());
                rootElement.appendChild(e);
            }


            rootElement.appendChild(buildModPackAsElement(doc, mods, profileName));

// Schreibt komplettes document in xml datei
            try (FileOutputStream output = new FileOutputStream(MainPageController.getGameDirectory() + "\\fs_mod_manager\\profiles.xml")) {
                writeXml(doc, output);
            } catch (IOException | TransformerException e) {
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static void loadProfile(String profileName) {

        ArrayList<Mod> profileList = MainPageController.getModpacks().get(profileName);
        File[] activeMods = MainPageController.getActiveModsFolder().listFiles();
        System.out.println("Profile contains the following Mods to load: " + profileList);

        //run through active mods folder to remove non-required mods into inactive folder
        for (File f : activeMods) {

            if (profileList.contains(f)) {
                //if the mod is already in the active mods folder
            } else {
                try {
                    Files.move(Paths.get(f.getPath()), Paths.get(MainPageController.getInactiveModsFolder().getPath() + "\\" + f.getName()));   //move unused files to inactive folder
                    System.out.println("Unused Mod: " + f.getName() + " moved to inactive folder");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File[] inactiveMods = MainPageController.getInactiveModsFolder().listFiles();
        //run through inactive mods folder to add required mods to active folder
        for (File f2 : inactiveMods) {

            if (profileList.contains(f2)) {

                try {
                    Files.move(Paths.get(f2.getPath()), Paths.get(MainPageController.getActiveModsFolder().getPath() + "\\" + f2.getName()));   //move required files to active folder
                    System.out.println("Required Mod: " + f2.getName() + " moved to active folder");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //if the mod is already in active folder
            }
        }


    }

    private static NodeList getElementsFromXML(File xmlFile) {

        Document doc = null;
        DocumentBuilder docBuilder = null;
        try {

            docBuilder = factory.newDocumentBuilder();
            doc = docBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return doc.getElementsByTagName("modpack");

    }

    /**
     * Creates a Hashmap where the key is the name of each profile and the value is an ArrayList of the corrosponding mod files
     *
     * @param xmlFile
     * @return
     */
    public static HashMap<String, ArrayList<Mod>> getModpacksFromXML(File xmlFile) {

        HashMap<String, ArrayList<Mod>> modpacks = new HashMap<>();

        try {
            String profileName = "";
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();


            NodeList list = doc.getElementsByTagName("modpack");

            for (int j = 0; j < list.getLength(); ++j) {
                ArrayList<Mod> mods = new ArrayList<>();
                profileName = list.item(j).getAttributes().getNamedItem("name").getTextContent();

                String[] fileNames = list.item(j).getTextContent().split(";");
//                String[] actualMods = Arrays.copyOf(fileNames, fileNames.length -1);


                for (String s : fileNames) {
                    mods.add(new Mod(MainPageController.getInactiveModsFolder().getPath() + "\\" + s));
                }
                modpacks.put(profileName, mods);


            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return modpacks;
    }

    /**
     * Create a String of mod names connected by ";" into an XML element
     *
     * @param doc
     * @param mods
     * @param name
     * @return
     */
    private static Element buildModPackAsElement(Document doc, ArrayList<String> mods, String name) {


        StringBuilder builder = new StringBuilder("");
        //iterate through all mods and add them to the string
        for (String s : mods) {
            builder.append(s + ";");
        }

        builder.deleteCharAt(builder.lastIndexOf(";")); //delete the ; after the last fileName to avoid adding an empty mod

        Element element = doc.createElement("modpack");
        element.setAttribute("name", name);
        element.appendChild(doc.createTextNode(builder.toString()));

        return element;
    }


    private static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

}
