package org.arl.fjage.gradle;

import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FjagePackagingTask extends DefaultTask {

    @TaskAction
    public void doPackage()
            throws IOException {
        final File outputDir = new File(getProject().getBuildDir(), "distributions");
        outputDir.mkdirs();
        final File outputFile = new File(outputDir, getProject().getName() + ".zip");
        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile))) {
            writeManifest(zipOutputStream);
            copy(zipOutputStream, "libs/", getProject().getConfigurations().getByName("runtimeClasspath"));
            copy(zipOutputStream, "libs/", getProject().getTasks().getByName("jar").getOutputs().getFiles());
        }
    }

    private void writeManifest(final ZipOutputStream zipOutputStream)
            throws IOException {
        final FjageManifest manifest = FjageManifestFactory.newManifest(getProject());
        zipOutputStream.putNextEntry(new ZipEntry("manifest.yaml"));
        FjageManifestWriter.writeManifest(manifest, zipOutputStream);
        zipOutputStream.closeEntry();
    }

    private void copy(ZipOutputStream zipOutputStream, String prefix, FileCollection files)
            throws IOException {
        for (final File file : files) {
            copy(zipOutputStream, prefix, file);
        }
    }

    private void copy(ZipOutputStream zipOutputStream, String prefix, File file)
            throws IOException {
        final String name = prefix + file.getName();
        final ZipEntry zipEntry = new ZipEntry(name);
        zipOutputStream.putNextEntry(zipEntry);
        try (final InputStream inputStream = new FileInputStream(file)) {
            IOUtils.copy(inputStream, zipOutputStream);
        }
        zipOutputStream.closeEntry();
    }
}
