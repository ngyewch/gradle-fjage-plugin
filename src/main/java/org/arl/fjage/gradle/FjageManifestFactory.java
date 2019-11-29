package org.arl.fjage.gradle;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

public class FjageManifestFactory {

    public static FjageManifest newManifest(Project project) {
        return new Builder(project)
                .build();
    }

    private static class Builder {

        private final Project project;
        private final FjageManifest manifest = new FjageManifest();

        private Builder(Project project) {
            super();

            this.project = project;
        }

        private FjageManifest build() {
            collectDependencies("fjage");
            collectDependencies("compileOnly");
            collectDependencies("implementation");
            collectDependencies("runtimeOnly");

            return manifest;
        }

        private void collectDependencies(String configurationName) {
            final List<String> list = manifest.getDependencies()
                    .computeIfAbsent(configurationName, k -> new ArrayList<>());
            project.getConfigurations().getByName(configurationName).getDependencies().forEach(dep ->
                    list.add(String.format("%s:%s:%s", dep.getGroup(), dep.getName(), dep.getVersion()))
            );
        }
    }
}
