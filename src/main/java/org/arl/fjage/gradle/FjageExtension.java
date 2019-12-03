package org.arl.fjage.gradle;

import java.io.File;

public class FjageExtension {

    private File mainSourceDirectory;

    private File testSourceDirectory;

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
}
