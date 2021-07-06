package org.azati.cources.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture {
    private BufferedImage image;
    private Color color;
    private String path;

    public Picture (String path) throws IOException {
        File file = new File(path);
        image = ImageIO.read(file);
        this.path = path;
    }

    private boolean saveImage() throws IOException {
        File result = new File(path);
        return ImageIO.write(image, path.substring(path.lastIndexOf('.')), result);
    }
}
