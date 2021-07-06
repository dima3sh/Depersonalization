package org.azati.cources.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture {
    private BufferedImage image;
    private String path;

    public Picture(String path) throws IOException {
        File file = new File(path);
        image = ImageIO.read(file);
        this.path = path;
    }

    public Integer getWidth() {
        return image.getWidth();
    }

    public Integer getHeight() {
        return image.getHeight();
    }

    public String getPath(){
        return path;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setColorPixel(Integer x, Integer y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

}
