package org.arl.fjage.gradle;

import org.gradle.api.file.FileTree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FjageExtension {

    private File mainSourceDirectory;
    private File testSourceDirectory;
    private final List<CopyIntoEntry> copyIntoEntries = new ArrayList<>();

    public File getMainSourceDirectory() {
        return mainSourceDirectory;
    }

    public void setMainSourceDirectory(File mainSourceDirectory) {
        this.mainSourceDirectory = mainSourceDirectory;
    }

    public File getTestSourceDirectory() {
        return testSourceDirectory;
    }

    public void setTestSourceDirectory(File testSourceDirectory) {
        this.testSourceDirectory = testSourceDirectory;
    }

    public void copyInto(FileTree fileTree) {
        copyIntoEntries.add(new CopyIntoEntry(null, fileTree));
    }

    public void copyInto(String destination, FileTree fileTree) {
        copyIntoEntries.add(new CopyIntoEntry(destination, fileTree));
    }

    public List<CopyIntoEntry> getCopyIntoEntries() {
        return copyIntoEntries;
    }

    public static class CopyIntoEntry {

        private final String destination;
        private final FileTree fileTree;

        public CopyIntoEntry(String destination, FileTree fileTree) {
            super();

            this.destination = normalize(destination);
            this.fileTree = fileTree;
        }

        private String normalize(String destination) {
            if (destination == null) {
                return "";
            }
            destination = destination.trim();
            if (destination.isEmpty()) {
                return "";
            }
            if (destination.endsWith("/")) {
                return destination;
            }
            return destination + "/";
        }

        public String getDestination() {
            return destination;
        }

        public FileTree getFileTree() {
            return fileTree;
        }
    }
}
