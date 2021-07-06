package org.azati.cources.services;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static File renameToZip(File docx) throws IOException {
        File newFile = new File(docx.getCanonicalPath().substring(0, docx.getCanonicalPath().indexOf('.'))
                + ".zip");
        docx.renameTo(newFile);
        return docx;
    }

    public static File renameToDocx(File zip) throws IOException {
        File newFile = new File(zip.getCanonicalPath().substring(0, zip.getCanonicalPath().indexOf('.')) + ".docx");
        zip.renameTo(newFile);

        return zip;
    }

    public static List<BufferedImage> getAllImages(File folder) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isFile() && isPicture(file.getCanonicalPath())) {
                images.add(ImageIO.read(file));
            } else if (file.isDirectory()) {
                images.addAll(getAllImages(file));
            }
        }
        return images;
    }

    public static File getDoc(File folder) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isFile() && isDocXML(file.getCanonicalPath())) {
                return file;
            } else if (file.isDirectory()) {
                return getDoc(file);
            }
        }
        throw new IOException("file not found");
    }

    private static Boolean isPicture(String path) {
        return path.substring(path.lastIndexOf('.')).matches("png|jpeg|jpg|gif");
    }

    private static Boolean isDocXML(String path) {
       return path.substring(path.lastIndexOf('/')).equals("document.xml");
    }
}
