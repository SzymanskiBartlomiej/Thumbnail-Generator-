package agh.edu.pl.ThumbnailGenerator.Model.DirectorySearch;

import agh.edu.pl.ThumbnailGenerator.Model.Image;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFiles {
    private final String path;
    private static final String[] allowedExtensions = {"png", "jpg"};

    public UnzipFiles(String path) {
        this.path = path;
    }

    public List<Image> readZip() throws IOException {
        List<Image> zipImages = new ArrayList<>();

        ZipInputStream zis = new ZipInputStream(new FileInputStream(path));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String processingFile = zipEntry.toString();
            int lastSlashIndex = processingFile.lastIndexOf("/");
            int lastDotIndex = processingFile.lastIndexOf(".");

            String fileName, fileExtension;

            if (lastDotIndex > 0 && lastDotIndex < processingFile.length() - 1) {
                fileName = processingFile.substring(lastSlashIndex + 1, lastDotIndex);
                fileExtension = processingFile.substring(lastDotIndex + 1);
                if (Arrays.asList(allowedExtensions).contains(fileExtension)) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;

                    while ((len = zis.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }

                    byte[] fileBytes = outputStream.toByteArray();
                    zipImages.add(new Image(fileName, fileExtension, fileBytes, ""));
                }
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        return zipImages;
    }
}
