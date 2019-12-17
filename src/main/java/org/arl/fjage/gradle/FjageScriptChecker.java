package org.arl.fjage.gradle;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.gradle.api.file.FileCollection;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FjageScriptChecker {

    private final GroovyClassLoader groovyClassLoader;
    private final CompilerConfiguration compilerConfiguration;

    public FjageScriptChecker(FileCollection classpath) {
        super();

        this.groovyClassLoader = new GroovyClassLoader();
        for (final File classpathElement : classpath) {
            groovyClassLoader.addClasspath(classpathElement.getPath());
        }
        this.compilerConfiguration = new CompilerConfiguration();
        this.compilerConfiguration.addCompilationCustomizers(new ImportCustomizer());
    }

    public void check(File scriptFile)
            throws IOException {
        final Binding binding = new Binding();
        final GroovyShell groovyShell = new GroovyShell(groovyClassLoader, binding, compilerConfiguration);
        groovyShell.parse(scriptFile);
    }

    public static void printErrors(ErrorCollector errorCollector, PrintWriter printWriter) {
        final Janitor janitor = new Janitor();
        try {
            errorCollector.write(printWriter, janitor);
            printWriter.flush();
        } finally {
            janitor.cleanup();
        }
    }
}