package org.azati.cources.services;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import static org.azati.cources.services.ZipService.unZipFile;
import static org.azati.cources.services.ZipService.zipFile;

public class Depersonalization {

    public static void readFiles(String path) throws IOException, ParserConfigurationException, SAXException {
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                canWeDoZip(file, path);
                System.out.println(file.getName());
            }
        }
    }

    public static void canWeDoZip(File file, String way) throws IOException, ParserConfigurationException, SAXException {
        File folderZip = new File(file.getCanonicalPath() + File.separator + "folder");
        if (!folderZip.exists()) {
            folderZip.mkdir();
        }
        File newFile = new File(file.getCanonicalPath().substring(0, file.getCanonicalPath().indexOf('.'))
                + ".zip");
        file.renameTo(newFile);
        unZipFile(file.getCanonicalPath());
        parseXML(file.getCanonicalPath().substring(0, file.getCanonicalPath().lastIndexOf('\\'))
                + File.separator + "folder/word/document.xml");
        String sourceFile = way + File.separator + "folder";
        FileOutputStream fos = new FileOutputStream(file.getCanonicalPath().substring(0,
                file.getCanonicalPath().indexOf('.')) + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();

        newFile = new File(file.getCanonicalPath().substring(0, file.getCanonicalPath().indexOf('.')) + ".docx");
        file.renameTo(newFile);


    }


    public static void parseXML(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(path));
        NodeList adsList = document.getDocumentElement().getElementsByTagName("ads");

        for (int i = 0; i < adsList.getLength(); i++) {
            adsList.item(i).setNodeValue("1");
            Node node = document.getElementsByTagName("ads").item(i).getFirstChild();
            node.setTextContent("1");
            System.out.println(node.getNodeValue());
            NamedNodeMap attributes = node.getAttributes();
        }
        saveXMLContent(document, path);
    }

    private static void saveXMLContent(Document document, String xmlFile) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xmlFile);
            transformer.transform(domSource, streamResult);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
