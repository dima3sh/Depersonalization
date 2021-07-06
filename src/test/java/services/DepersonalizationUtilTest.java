package services;

import org.azati.cources.entity.Picture;
import org.azati.cources.services.DepersonalizationUtil;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepersonalizationUtilTest {
    @Test
    public void readFileTest() throws IOException, ParserConfigurationException, SAXException {
        File file = new File("src\\main\\resources\\programmist\\программист (с фото).docx");
        DepersonalizationUtil.depersonalize(file);
    }

    @Test
    public void depImage() throws IOException {
        Picture pic = new Picture("C:\\java_projects\\Depersonalization\\src\\main\\resources\\1.jpg");
        List<Picture> list= new ArrayList<>(Arrays.asList(pic));
        DepersonalizationUtil.depersonalizeImages(list);
    }
}
