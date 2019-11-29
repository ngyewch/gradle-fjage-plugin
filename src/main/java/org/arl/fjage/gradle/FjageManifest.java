package org.arl.fjage.gradle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FjageManifest {

    private Map<String, List<String>> dependencies = new LinkedHashMap<>();

    public Map<String, List<String>> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, List<String>> dependencies) {
        this.dependencies = dependencies;
    }
}
