package org.arl.fjage.gradle;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipOutputStreamHelper {

    private final ZipOutputStream zipOutputStream;

    public ZipOutputStreamHelper(ZipOutputStream zipOutputStream) {
        super();

        this.zipOutputStream = zipOutputStream;
    }

    public void putFile(File file, String name)
            throws IOException {
        final ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setSize(file.length());
        zipEntry.setLastModifiedTime(FileTime.fromMillis(file.lastModified()));
        zipOutputStream.putNextEntry(zipEntry);
        copy(file, zipOutputStream);
        zipOutputStream.closeEntry();
    }

    private static void copy(File file, OutputStream outputStream)
            throws IOException {
        try (final InputStream inputStream = new FileInputStream(file)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
