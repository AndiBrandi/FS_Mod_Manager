package com.modmanager.fsmodmanager;

import java.io.*;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static java.lang.System.in;


public class VersionReader {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        //System.out.println(getVersion("C:/Users/andre/Documents/My Games/FarmingSimulator2022/mods/FS22_WestBridgeHills22.zip"));                                                             //Just for Testing
    }
    public static String getVersion(String pathname){
            File f = new File(pathname);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedInputStream bfis = new BufferedInputStream(fis);
            ZipInputStream zis = new ZipInputStream(bfis);

            ZipEntry zipEntry;
            //int numEntry = 0;                                                                                                                                                                 //Needed for Stats
            while (true) {
                try {
                    if (!((zipEntry = zis.getNextEntry()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //numEntry++;                                                                                                                                                                   //Needed for Stats
                if(zipEntry.getName().equals("modDesc.xml")) {
                    //System.out.format("Entry #%d: path=%s, size=%d, compressed size=%d \n", numEntry, zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize());                  //Stats
                    byte[] buffer = new byte[2048];
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len;
                    // read bytes of file
                    while (true) {
                        try {
                            if (!((len = zis.read(buffer)) > 0)) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        //System.out.println("file: " + zipEntry.getName() + " : " + len + "  bytes read");
                        bos.write(buffer, 0, len);
                    }
                    // convert bytes to string
                    byte[] zipFileBytes = bos.toByteArray();
                    String fileContent = new String(zipFileBytes);
                    String teil1 = fileContent.substring(fileContent.indexOf("<version>"), fileContent.indexOf("</version>"));
                    teil1 = teil1.substring(teil1.indexOf(">")+1);

                    return teil1;
                }
            }
            // remember to close the zip input stream!
        try {
            zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* Would Work if the modDesc isnt in a zip File
        try {
            File file = new File(pathname);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            //document.getDocumentElement().normalize();
            System.out.println("Root Element: " + document.getDocumentElement().getNodeName());
            NodeList nlist = document.getElementsByTagName("version");
            System.out.println("Version: " + nlist.item(0).getTextContent());
            System.out.println("----------------------------");
            return nlist.item(0).getTextContent();
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    */
}
