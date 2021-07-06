package services;

import org.azati.cources.services.Depersonalization;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DepersonalizationTest {
    @Test
    public void readFileTest() throws IOException, ParserConfigurationException, SAXException {
        Depersonalization.readFiles("src\\main\\resources\\programmist");
    }
}
