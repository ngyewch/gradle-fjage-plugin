package org.arl.fjage.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

public class FjagePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.afterEvaluate(p -> {
            final Configuration fjageConfiguration = project.getConfigurations()
                    .create("fjage");
            final Configuration compileClasspathConfiguration = project.getConfigurations()
                    .getByName("compileClasspath");
            final Configuration testImplementationConfiguration = project.getConfigurations()
                    .getByName("testImplementation");
            compileClasspathConfiguration.extendsFrom(fjageConfiguration);
            testImplementationConfiguration.extendsFrom(fjageConfiguration);

            project.getTasks().register("packageFjage", FjagePackagingTask.class,
                    task -> task.dependsOn("jar"));
            project.getTasks().getByName("assemble")
                    .dependsOn("packageFjage");

            project.getTasks().register("fjageGroovyBoot", FjageGroovyBootTask.class,
                    task -> task.dependsOn("classes"));
        });
    }
}
