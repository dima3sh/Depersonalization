package services;

import org.azati.cources.services.Depersonalization;
import org.junit.Test;

import java.io.IOException;

public class DepersonalizationTest {
    @Test
    public void readFileTest(){
        Depersonalization.readFiles("src\\main\\resources\\programmist");
    }
}
