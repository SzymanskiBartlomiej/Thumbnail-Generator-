package agh.edu.pl.util;

import agh.edu.pl.controller.DirectoryTreeController;
import agh.edu.pl.model.Picture;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipZip {
    private final byte[] zipBytes;
    private static final String[] allowedExtensions = {"png", "jpg"};

    public UnzipZip(byte[] zipBytes) {
        this.zipBytes = zipBytes;
    }

    public List<Picture> readZip() throws IOException {
        List<Picture> zipImages = new ArrayList<>();

        try (ByteArrayInputStream bis = new ByteArrayInputStream(zipBytes);
             ZipInputStream zis = new ZipInputStream(bis)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String processingFile = zipEntry.toString();
                int lastSlashIndex = processingFile.lastIndexOf("/");
                int lastDotIndex = processingFile.lastIndexOf(".");

                String fileName, fileExtension, path = DirectoryTreeController.rootFolder;;

                if (lastDotIndex > 0 && lastDotIndex < processingFile.length() - 1) {
                    fileName = processingFile.substring(lastSlashIndex + 1, lastDotIndex);
                    fileExtension = processingFile.substring(lastDotIndex + 1);
                    path = lastSlashIndex != -1 ? path + "/" + processingFile.substring(0, lastSlashIndex) : path;
                    if (Arrays.asList(allowedExtensions).contains(fileExtension)) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;

                        while ((len = zis.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                        }

                        byte[] fileBytes = outputStream.toByteArray();
                        zipImages.add(new Picture(fileBytes, fileName, fileExtension, path));
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        }

        return zipImages;
    }
}
