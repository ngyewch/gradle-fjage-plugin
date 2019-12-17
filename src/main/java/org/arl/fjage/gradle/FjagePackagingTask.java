package org.arl.fjage.gradle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FjagePackagingTask extends DefaultTask {

    @Classpath
    public FileCollection mainClasspath = getProject().getConvention().getPlugin(JavaPluginConvention.class)
            .getSourceSets().getByName("main").getCompileClasspath();

    @Classpath
    public FileCollection testClasspath = getProject().getConvention().getPlugin(JavaPluginConvention.class)
            .getSourceSets().getByName("test").getRuntimeClasspath();

    public FileCollection getMainClasspath() {
        return mainClasspath;
    }

    public void setMainClasspath(FileCollection mainClasspath) {
        this.mainClasspath = mainClasspath;
    }

    public FileCollection getTestClasspath() {
        return testClasspath;
    }

    public void setTestClasspath(FileCollection testClasspath) {
        this.testClasspath = testClasspath;
    }

    @TaskAction
    public void doPackage()
            throws IOException {
        final FjageExtension fjageExtension = getProject().getExtensions().getByType(FjageExtension.class);

        final ErrorCollectorHolder errorCollectorHolder = new ErrorCollectorHolder();
        checkScripts(fjageExtension.getMainSourceDirectory(), mainClasspath, errorCollectorHolder);
        checkScripts(fjageExtension.getTestSourceDirectory(), testClasspath, errorCollectorHolder);
        if (errorCollectorHolder.getErrorCollector() != null) {
            FjageScriptChecker.printErrors(errorCollectorHolder.getErrorCollector(), new PrintWriter(System.out));
            throw new GradleException("Errors in script files");
        }

        final File outputDir = new File(getProject().getBuildDir(), "distributions");
        outputDir.mkdirs();
        final File outputFile = new File(outputDir, getProject().getName() + ".zip");
        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile))) {
            final ZipOutputStreamHelper zipOutputStreamHelper = new ZipOutputStreamHelper(zipOutputStream);
            writeManifest(zipOutputStream);
            copy(zipOutputStreamHelper, "libs/",
                    getProject().getConfigurations().getByName("runtimeClasspath"));
            copy(zipOutputStreamHelper, "libs/",
                    getProject().getTasks().getByName("jar").getOutputs().getFiles());
            if (fjageExtension.getMainSourceDirectory().isDirectory()) {
                copyDirectory(zipOutputStreamHelper, "", fjageExtension.getMainSourceDirectory());
            }
            for (final FjageExtension.CopyIntoEntry copyIntoEntry : fjageExtension.getCopyIntoEntries()) {
                copy(zipOutputStreamHelper, copyIntoEntry);
            }
        }
    }

    private void checkScripts(File baseDirectory, FileCollection classpath, ErrorCollectorHolder errorCollectorHolder)
            throws IOException {
        final FjageScriptChecker scriptChecker = new FjageScriptChecker(classpath);
        checkScripts(new File(baseDirectory, "etc"), scriptChecker, errorCollectorHolder);
        checkScripts(new File(baseDirectory, "scripts"), scriptChecker, errorCollectorHolder);
    }

    private void checkScripts(File scriptsDirectory, FjageScriptChecker scriptChecker,
                              ErrorCollectorHolder errorCollectorHolder)
            throws IOException {
        if (!scriptsDirectory.isDirectory()) {
            return;
        }
        for (final File scriptFile : FileUtils.listFiles(scriptsDirectory, new String[]{"groovy"}, true)) {
            try {
                scriptChecker.check(scriptFile);
            } catch (MultipleCompilationErrorsException e) {
                errorCollectorHolder.add(e.getErrorCollector());
            }
        }
    }

    private void writeManifest(final ZipOutputStream zipOutputStream)
            throws IOException {
        final FjageManifest manifest = FjageManifestFactory.newManifest(getProject());
        zipOutputStream.putNextEntry(new ZipEntry("manifest.yaml"));
        FjageManifestWriter.writeManifest(manifest, zipOutputStream);
        zipOutputStream.closeEntry();
    }

    private void copyDirectory(ZipOutputStreamHelper zipOutputStreamHelper, String prefix, File directory)
            throws IOException {
        final Iterator<File> fileIterator = FileUtils.iterateFiles(directory, TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE);
        while (fileIterator.hasNext()) {
            final File file = fileIterator.next();
            final String relativePath = directory.toURI().relativize(file.toURI()).toString();
            zipOutputStreamHelper.putFile(file, prefix + relativePath);
        }
    }

    private void copy(ZipOutputStreamHelper zipOutputStreamHelper, FjageExtension.CopyIntoEntry copyIntoEntry) {
        copyIntoEntry.getFileTree().visit(fileVisitDetails -> {
            if (!fileVisitDetails.isDirectory()) {
                try {
                    zipOutputStreamHelper.putFile(fileVisitDetails.getFile(),
                            copyIntoEntry.getDestination() + fileVisitDetails.getRelativePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void copy(ZipOutputStreamHelper zipOutputStreamHelper, String prefix, FileCollection files)
            throws IOException {
        for (final File file : files) {
            zipOutputStreamHelper.putFile(file, prefix + file.getName());
        }
    }
}
