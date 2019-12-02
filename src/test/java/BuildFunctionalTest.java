import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BuildFunctionalTest {

    @Rule
    public final TemporaryFolder rootDirectory = new TemporaryFolder();
    private File project1Dir;

    private List<File> pluginClasspath;

    @Before
    public void setup()
            throws IOException {
        pluginClasspath = PluginTestUtils.readFileCollectionFromResource("plugin-classpath.txt");

        ZipUtils.unzipFromResource("test.zip", rootDirectory.getRoot());
        project1Dir = new File(rootDirectory.getRoot(), "project1");
    }

    @Test
    public void testProject1Build() {
        final BuildResult result = GradleRunner.create()
                .withProjectDir(project1Dir)
                .withArguments("build")
                .withPluginClasspath(pluginClasspath)
                .forwardOutput()
                .build();
    }

    @Test
    public void testProject1Run() {
        final BuildResult result = GradleRunner.create()
                .withProjectDir(project1Dir)
                .withArguments("test1")
                .withPluginClasspath(pluginClasspath)
                .forwardOutput()
                .build();
    }

    private void dumpDirectory() {
        FileUtils.iterateFiles(project1Dir, null, true)
                .forEachRemaining(f -> {
                    final String relativePath = project1Dir.toURI().relativize(f.toURI()).toString();
                    if (relativePath.startsWith(".gradle/")) {
                        return;
                    }
                    System.out.println(relativePath);
                });
    }
}
