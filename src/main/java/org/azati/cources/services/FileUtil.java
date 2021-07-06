package org.azati.cources.services;

import org.azati.cources.entity.Picture;

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

    public static List<Picture> getAllImages(File folder) throws IOException {
        List<Picture> images = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isFile() && isPicture(file.getCanonicalPath())) {
                images.add(new Picture(file.getCanonicalPath()));
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
            } else if (file.isDirectory() && file.getCanonicalPath().substring(file.getCanonicalPath().lastIndexOf('\\')).equals(folder)) {
                return getDoc(file);
            }
        }
        return new File(folder.getCanonicalPath() + File.separator + "word" + File.separator + "document.xml");
    }

    private static Boolean isPicture(String path) {
        return path.substring(path.lastIndexOf('.') + 1).matches("png|jpeg|jpg|gif");
    }

    private static Boolean isDocXML(String path) {
        return path.substring(path.lastIndexOf('\\')).equals("document.xml");
    }



    public static boolean saveImage(BufferedImage image, String path) {
        File result = new File(path);
        try {
            return ImageIO.write(image, path.substring(path.lastIndexOf('.') + 1), result);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
