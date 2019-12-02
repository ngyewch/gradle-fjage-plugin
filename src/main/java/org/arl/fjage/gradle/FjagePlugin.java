package org.arl.fjage.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

public class FjagePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final Configuration fjageConfiguration = project.getConfigurations()
                .create("fjage");
        project.getTasks().register("packageFjage", FjagePackagingTask.class,
                task -> task.dependsOn("jar"));

        project.afterEvaluate(p -> {
            final Configuration compileClasspathConfiguration = project.getConfigurations()
                    .getByName("compileClasspath");
            final Configuration testImplementationConfiguration = project.getConfigurations()
                    .getByName("testImplementation");
            compileClasspathConfiguration.extendsFrom(fjageConfiguration);
            testImplementationConfiguration.extendsFrom(fjageConfiguration);

            project.getTasks().getByName("assemble")
                    .dependsOn("packageFjage");
        });
    }
}
