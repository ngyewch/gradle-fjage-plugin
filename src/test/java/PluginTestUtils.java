import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PluginTestUtils {

    public static List<File> readFileCollectionFromResource(String resourceName)
            throws IOException {
        return readFileCollectionFromResource(Thread.currentThread().getContextClassLoader(), resourceName);
    }

    public static List<File> readFileCollectionFromResource(ClassLoader classLoader, String resourceName)
            throws IOException {
        try (final InputStream inputStream = classLoader.getResourceAsStream((resourceName))) {
            return readFileCollection(inputStream);
        }
    }

    public static List<File> readFileCollection(InputStream inputStream)
            throws IOException {
        final List<File> files = new ArrayList<>();
        try (final LineIterator lineIterator = IOUtils.lineIterator(inputStream, StandardCharsets.UTF_8)) {
            while (lineIterator.hasNext()) {
                final String line = lineIterator.nextLine();
                if (line == null) {
                    break;
                }
                files.add(new File(line));
            }
        }
        return files;
    }
}
