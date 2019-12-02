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

    private List<File> pluginClasspath;

    @Before
    public void setup()
            throws IOException {
        pluginClasspath = PluginTestUtils.readFileCollectionFromResource("plugin-classpath.txt");
        ZipUtils.unzipFromResource("test.zip", rootDirectory.getRoot());
    }

    @Test
    public void testProject1() {
        final BuildResult result = GradleRunner.create()
                .withProjectDir(new File(rootDirectory.getRoot(), "project1"))
                .withArguments("build")
                .withPluginClasspath(pluginClasspath)
                .build();
    }
}
