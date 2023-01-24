package com.modmanager.fsmodmanager;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class VersionReader {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        //System.out.println(getVersion("C:/Users/andre/Documents/My Games/FarmingSimulator2022/mods/FS22_WestBridgeHills22.zip"));                                                             //Just for Testing
    }

    public static String getVersion(String pathname) {
        File f = new File(pathname);
        BufferedInputStream bfis = null;

        try {
            bfis = new BufferedInputStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ZipInputStream zis = new ZipInputStream(bfis);
        ZipFile zif = null;
        InputStream zifInputStream = null;


        try {
            zif = new ZipFile(f);
            System.out.println(zif.getName());
            zifInputStream = zif.getInputStream(zif.getEntry("modDesc.xml"));

//            System.out.println(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ZipEntry zipEntry;

        //int numEntry = 0;                                                                                                                                                                 //Needed for Stats


        //numEntry++;                                                                                                                                                                   //Needed for Stats
        //System.out.format("Entry #%d: path=%s, size=%d, compressed size=%d \n", numEntry, zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize());                  //Stats
        byte[] buffer = new byte[2048];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;

        // read bytes of file
        while (true) {
            try {
                //
                if (!((len = zifInputStream.read(buffer)) > 0)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bos.write(buffer, 0, len);
        }
        // convert bytes to string
        byte[] zipFileBytes = bos.toByteArray();
        String fileContent = new String(zipFileBytes);
        String teil1 = fileContent.substring(fileContent.indexOf("<version>"), fileContent.indexOf("</version>"));
        teil1 = teil1.substring(teil1.indexOf(">") + 1);

        try {
            zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return teil1;
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

