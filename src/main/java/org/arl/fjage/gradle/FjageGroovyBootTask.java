package org.arl.fjage.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FjageGroovyBootTask extends DefaultTask {

    private static final String RES_PREFIX = "res:/";
    private static final String CLS_PREFIX = "cls://";
    private static final String GROOVY_EXTENSION = ".groovy";

    @InputDirectory
    public File scriptsDirectory = getProject().file("src/fjage/groovy");

    @Input
    public List<String> scripts = Collections.EMPTY_LIST;

    @Classpath
    public FileCollection classpath = getProject().getConvention().getPlugin(JavaPluginConvention.class)
            .getSourceSets().getByName("test").getRuntimeClasspath();

    @Input
    public Map<String, ?> systemProperties = Collections.EMPTY_MAP;

    public FjageGroovyBootTask() {
        super();

        dependsOn("classes");
    }

    @TaskAction
    public void doBoot()
            throws IOException {
        final List<String> scriptLocations = new ArrayList<>();
        for (final String scriptFilename : scripts) {
            if (scriptFilename.startsWith(RES_PREFIX)) {
                scriptLocations.add(normalizeRes(scriptFilename));
            } else if (scriptFilename.startsWith(CLS_PREFIX)) {
                scriptLocations.add(normalizeCls(scriptFilename));
            } else {
                scriptLocations.add(normalizeFile(scriptFilename));
            }
        }

        new File("logs").mkdirs();

        final JavaExec javaExec = getProject().getTasks().create("fjageGroovyBootJavaExec", JavaExec.class);
        javaExec.setMain("org.arl.fjage.shell.GroovyBoot");
        javaExec.setArgs(scriptLocations);
        javaExec.setSystemProperties(systemProperties);
        javaExec.setClasspath(classpath);
        javaExec.setStandardInput(System.in);
        javaExec.setStandardOutput(System.out);
        javaExec.setErrorOutput(System.err);
        javaExec.exec();
    }

    private String normalizeRes(String scriptLocation)
            throws IOException {
        final boolean verify = false;
        if (verify) {
            String path = scriptLocation.substring(RES_PREFIX.length());
            if (!path.endsWith(GROOVY_EXTENSION)) {
                path += GROOVY_EXTENSION;
            }
            try (final InputStream inputStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(path)) {
                if (inputStream == null) {
                    throw new FileNotFoundException(scriptLocation);
                }
            }
        }
        return scriptLocation;
    }

    private String normalizeCls(String scriptLocation)
            throws FileNotFoundException {
        final boolean verify = false;
        if (verify) {
            final String className = scriptLocation.substring(CLS_PREFIX.length());
            try {
                final Class<?> cls = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new FileNotFoundException(scriptLocation);
            }
        }
        return scriptLocation;
    }

    private String normalizeFile(String scriptLocation)
            throws FileNotFoundException {
        final URI baseUri = getProject().getProjectDir().toURI();
        File scriptFile = new File(scriptsDirectory, scriptLocation);
        if (scriptFile.isFile()) {
            return baseUri.relativize(scriptFile.toURI()).toString();
        } else if (scriptLocation.endsWith(GROOVY_EXTENSION)) {
            throw new FileNotFoundException(scriptFile.getPath());
        } else {
            scriptFile = new File(scriptsDirectory, scriptLocation + GROOVY_EXTENSION);
            if (scriptFile.isFile()) {
                return baseUri.relativize(scriptFile.toURI()).toString();
            } else {
                throw new FileNotFoundException(scriptFile.getPath());
            }
        }
    }
}
