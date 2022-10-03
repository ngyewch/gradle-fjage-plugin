import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    public static void unzipFromResource(String resourceName, File outputDirectory)
            throws IOException {
        unzipFromResource(Thread.currentThread().getContextClassLoader(), resourceName, outputDirectory);
    }

    public static void unzipFromResource(ClassLoader classLoader, String resourceName, File outputDirectory)
            throws IOException {
        try (final InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new FileNotFoundException(String.format("[resource] %s", resourceName));
            }
            unzip(inputStream, outputDirectory);
        }
    }

    public static void unzip(InputStream inputStream, File outputDirectory)
            throws IOException {
        try (final ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            while (true) {
                final ZipEntry zipEntry = zipInputStream.getNextEntry();
                if (zipEntry == null) {
                    break;
                }
                if (!zipEntry.isDirectory()) {
                    final File outputFile = new File(outputDirectory, zipEntry.getName());
                    if (!outputFile.toPath().normalize().startsWith(outputDirectory.toPath().normalize())) {
                        throw new IOException("Bad zip entry");
                    }
                    final File parentDirectory = outputFile.getParentFile();
                    if (parentDirectory != null) {
                        parentDirectory.mkdirs();
                    }
                    try (final OutputStream outputStream = new FileOutputStream(outputFile)) {
                        IOUtils.copy(zipInputStream, outputStream);
                    }
                }
            }
        }
    }
}
