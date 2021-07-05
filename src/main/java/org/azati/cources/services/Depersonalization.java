package org.azati.cources.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Depersonalization {

    public static void readFiles(String path) throws IOException {
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                canWeDoZip(file);
                System.out.println(file.getName());
            }
        }
    }

    public static void canWeDoZip(File file) throws IOException {
        File folderZip = new File(file.getCanonicalPath() + File.separator + "folder");
        if (!folderZip.exists()) {
            folderZip.mkdir();
        }
        File newFile = new File(file.getCanonicalPath().substring(0, file.getCanonicalPath().indexOf('.')) + ".zip");
        if (file.renameTo(newFile)) {
            System.out.println("Файл переименован успешно");
            ;
        } else {
            System.out.println("Файл не был переименован");
        }
        unZipFile(file.getCanonicalPath());


    }

    public static void unZipFile(String path) throws IOException {
        String fileZip = path;
        File destDir = new File("src/main/resources/programmist/folder");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
        }
        zis.closeEntry();
        zis.close();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }


}
