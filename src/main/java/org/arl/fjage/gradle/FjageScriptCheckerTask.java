package org.arl.fjage.gradle;

import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FjageScriptCheckerTask extends DefaultTask {

    @InputFiles
    public FileCollection scripts;

    @Classpath
    public FileCollection classpath = getProject().getConvention().getPlugin(JavaPluginConvention.class)
            .getSourceSets().getByName("main").getCompileClasspath();

    public FjageScriptCheckerTask() {
        super();

        dependsOn("classes");
    }

    public FileCollection getScripts() {
        return scripts;
    }

    public void setScripts(FileCollection scripts) {
        this.scripts = scripts;
    }

    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }

    @TaskAction
    public void check()
            throws IOException {
        final FjageScriptChecker scriptChecker = new FjageScriptChecker(classpath);
        final ErrorCollectorHolder errorCollectorHolder = new ErrorCollectorHolder();
        for (final File script : scripts) {
            try {
                scriptChecker.check(script);
            } catch (MultipleCompilationErrorsException e) {
                errorCollectorHolder.add(e.getErrorCollector());
            }
        }
        if (errorCollectorHolder.getErrorCollector() != null) {
            FjageScriptChecker.printErrors(errorCollectorHolder.getErrorCollector(), new PrintWriter(System.out));
            throw new GradleException("Errors in script files");
        }
    }
}
