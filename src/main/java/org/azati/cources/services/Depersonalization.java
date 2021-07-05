package org.azati.cources.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Depersonalization {

    public static void readFiles(String path) {
        File dir = new File(path);
        List<File> list = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                list.add(file);
                canWeDoZip(file);
                System.out.println(file.getName());
            }
        }
    }

    public static void canWeDoZip(File file) {
        try(ZipInputStream zin = new ZipInputStream(new FileInputStream(file.getAbsolutePath())))
        {
            ZipEntry entry;
            String name;
            long size;
            while((entry=zin.getNextEntry())!=null){

                name = entry.getName(); // получим название файла
                size=entry.getSize();  // получим его размер в байтах
                System.out.printf("File name: %s \t File size: %d \n", name, size);
                if(name.equals("word/document.xml")){
                    Scanner s = new Scanner(zin).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";
                    System.out.println(result);
                }

            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }

    public static void readXml(File file){
        try(FileReader reader = new FileReader(file.getAbsolutePath()))
        {
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
