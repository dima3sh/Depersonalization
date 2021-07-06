package services;

import org.azati.cources.services.DepersonalizationUtil;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DepersonalizationUtilTest {
    @Test
    public void readFileTest() throws IOException, ParserConfigurationException, SAXException {
        DepersonalizationUtil.readFiles("src\\main\\resources\\programmist");
    }
}
