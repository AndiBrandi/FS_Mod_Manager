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
 * ModPack Manager that uses XML format to store profiles
 */
public class ModPackManagerXML {

    static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    /**
     * erstellt neues <modpack></modpack> Element das den namen als Attribut, und die Mods als Value hat
     * <modpack name="Attribut-value">Value</modpack>
     *
     * @param mods
     * @param profileName
     */
    public static void createNewProfile(ArrayList<String> mods, String profileName) {

        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element rootElement = doc.createElement("profiles");
            doc.appendChild(rootElement);


//            doc.createElement("modpack")

            //re-add existing profiles
            NodeList nodes = getElementsFromXML(MainPageController.getProfilesXmlFile());
            for (int j = 0; j <= nodes.getLength() - 1; ++j) {                                      //for schleife hängt alle bereits in der datei existierenden Modpacks wieder an
                Element e = doc.createElement("modpack");
                e.setAttribute("name", nodes.item(j).getAttributes().getNamedItem("name").getTextContent());
                e.setTextContent(nodes.item(j).getTextContent());
                rootElement.appendChild(e);
            }


            rootElement.appendChild(buildModPackAsElement(doc, mods, profileName));                 //hängt neues <modpack> Element zu den bereits existierenden modpacks dazu

// Schreibt komplettes document in xml datei
            try (FileOutputStream output = new FileOutputStream(MainPageController.getGameDirectoryFolder() + "\\fs_mod_manager\\profiles.xml")) {
                writeXml(doc, output);
            } catch (IOException | TransformerException e) {
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static void deleteProfile(String profileName) {

        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();                                                 //wird für die nächste Zeile gebraucht
            Document doc = documentBuilder.newDocument();                                                                   //neues XML Dokument erstellen
            Element rootElement = doc.createElement("profiles");                                                    //Hauptelement <profiles> erstellen
            doc.appendChild(rootElement);                                                                                   //Hauptelement ans XML Dokument anhängen

            //re-add existing profiles except the one to delete
            NodeList nodes = getElementsFromXML(MainPageController.getProfilesXmlFile());                                   //NodeList ist eine Liste an XML elementen, wie z.B unser <modpack> element
            for (int j = 0; j <= nodes.getLength() - 1; ++j) {                                                              // Die for schleife durchläuft alle <modpack> elemente im XML file
                if (!nodes.item(j).getAttributes().getNamedItem("name").getTextContent().equals(profileName)) {              //in diesem if wird jedes XML Element (jedes Profil) wieder an das dokument angehängt
                    Element e = doc.createElement("modpack");                                                       //      ausser das zu löschende profil
                    e.setAttribute("name", nodes.item(j).getAttributes().getNamedItem("name").getTextContent());
                    e.setTextContent(nodes.item(j).getTextContent());
                    rootElement.appendChild(e);
                }
            }


            try (FileOutputStream output = new FileOutputStream(MainPageController.getGameDirectoryFolder() + "\\fs_mod_manager\\profiles.xml")) {        //try with resources (ka. hobs nd zaumbrocht dass i den fileStream nachher wieder schließ xD)
                writeXml(doc, output);                                                                                       // Schreibt komplettes document in xml datei
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
            doc = docBuilder.newDocument();
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


            NodeList list = doc.getElementsByTagName("modpack");                        //liefert eine Liste aller <modpack> Elemente im profiles.xml file

            for (int j = 0; j < list.getLength(); ++j) {
                ArrayList<Mod> mods = new ArrayList<>();                                                    //Arraylist die jeden Moddatei-namen eines Modpacks beinhaltet
                profileName = list.item(j).getAttributes().getNamedItem("name").getTextContent();           //lest den namen des modpacks

                String[] fileNames = list.item(j).getTextContent().split(";");                        //teilt den gesamten string auf in Array mit allen Moddatei-namen

                for (String s : fileNames) {
                    mods.add(new Mod(MainPageController.getInactiveModsFolder().getPath() + "\\" + s));         //fügt alle Mods zur Arraylist hinzu
                }
                modpacks.put(profileName, mods);                            //erstellt neuen eintrag in der Hashmap. Key = name des modpacks, Value = Arraylist mit Moddatei-namen


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
        for (String s : mods) {                                         //für jeden Mod der im neuen profil dabei ist wird die Schleife durchlaufen
            builder.append(s + ";");                                    //baut einen String zusammen sodass er wiefolgt aussieht: "Moddatei1;Moddatei2;Moddatei3..."
        }

        builder.deleteCharAt(builder.lastIndexOf(";")); //delete the ; after the last fileName to avoid adding an empty mod

        Element element = doc.createElement("modpack");                                         //Erstellt neues <modpack> Element
        element.setAttribute("name", name);                                                       //gibt dem Element einen namen damit es identifiziert werden kann
        element.appendChild(doc.createTextNode(builder.toString()));                                    //hängt zwischen die beiden Klammern <modapck> und </modpack> die Liste der Moddatei namen in form von Strings

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
