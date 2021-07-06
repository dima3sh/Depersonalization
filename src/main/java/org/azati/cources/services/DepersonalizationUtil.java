package org.azati.cources.services;

import org.azati.cources.constans.Constants;
import org.azati.cources.entity.Picture;
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
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DepersonalizationUtil {

    private static final String REPLACE_STR = "[Данные удалены]";

    public static void readFiles(String path) throws IOException, ParserConfigurationException, SAXException {
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                //canWeDoZip(file, path);
                System.out.println(file.getName());
            }
        }
    }

    public static File depersonalize(File file) throws IOException, ParserConfigurationException, SAXException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config/config.properties");
        properties.load(fis);

        String path = file.getCanonicalPath();
        String zipPath = path.substring(0, path.indexOf('.')) + ".zip";
        FileUtil.renameToZip(file);
        File folder = ZipUtil.unZip(zipPath);
        File docFile = FileUtil.getDoc(folder);
        parseXML(docFile);
        depersonalizeImages(FileUtil.getAllImages(new File(properties.getProperty("pathFolder"))));
        ZipUtil.zip(new File(properties.getProperty("pathFolder")), zipPath);
        FileUtil.renameToDocx(new File(zipPath));
        new File(properties.getProperty("pathFolder")).delete();
        return file;
    }

    public static void depersonalizeImages(List<Picture> images) {
        images.forEach(image -> {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    image.setColorPixel(i, j, new Color(0, 0, 0));
                }
            }
            FileUtil.saveImage(image.getImage(), image.getPath());
        });
    }

    public static void parseXML(File docFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(docFile);
        NodeList nodeList = document.getDocumentElement().getElementsByTagName(Constants.INFOTAG);

        Integer firstIndex = 0;
        Integer lastIndex = 0;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = document.getElementsByTagName(Constants.INFOTAG).item(i).getFirstChild();
            String textNode = node.getTextContent();
            if (textNode.contains(":")) {
                if (Constants.BIRTHDAY.equals(textNode.substring(0, textNode.indexOf(':')))) {
                    lastIndex = i;
                    node.setNodeValue(Constants.BIRTHDAY + ": " + REPLACE_STR);
                    while (firstIndex < lastIndex) {
                        Node oldNode = nodeList.item(firstIndex).getFirstChild();
                        if (firstIndex == 0) {
                            oldNode.setNodeValue(REPLACE_STR);
                        } else {
                            oldNode.setNodeValue("");
                        }
                        firstIndex++;
                    }
                }

                if (textNode.substring(0, textNode.indexOf(':')).equals(Constants.CITY)) {
                    node.setNodeValue(Constants.CITY + ": " + REPLACE_STR + '\t');
                }

                if (Constants.PHONE.equals(textNode.substring(0, textNode.indexOf(':')))) {
                    node.setNodeValue(Constants.PHONE + ": " + REPLACE_STR);
                    textNode = nodeList.item(++i).getFirstChild().getNodeValue();
                    while (!textNode.contains(":")) {
                        nodeList.item(i).getFirstChild().setNodeValue("");
                        i++;
                        textNode = nodeList.item(i).getFirstChild().getNodeValue();
                    }

                    if (Constants.EMAIL.equals(textNode.substring(0, textNode.indexOf(':')))) {
                        nodeList.item(++i).getFirstChild().setNodeValue(REPLACE_STR);
                    }

                }

            } else {
                Pattern pattern = Pattern.compile(".+?\\s?–\\s?.+?\\.\\s");
                Matcher matcher = pattern.matcher(textNode);
                if(matcher.find()) {
                    nodeList.item(i).getFirstChild().setNodeValue(matcher.replaceAll(REPLACE_STR));
                }
            }
            System.out.println(node.getNodeValue());
            NamedNodeMap attributes = node.getAttributes();
        }
        saveXMLContent(document, docFile.getCanonicalPath());
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
