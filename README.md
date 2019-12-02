# gradle-fjage-plugin

Gradle plugin for the [fjåge](https://github.com/org-arl/fjage) framework.

![GitHub release (latest by date)](https://img.shields.io/github/v/release/ngyewch/gradle-fjage-plugin)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ngyewch/gradle-fjage-plugin/Java%20CI)

## Sample usage

### Kotlin DSL

```kotlin
plugins {
  id("com.github.ngyewch.gradle-fjage-plugin") version "0.0.3"
}

dependencies {
    "fjage"("com.github.org-arl:fjage:1.6.2")
    "fjage"("org.codehaus.groovy:groovy-all:2.5.8")
    implementation("org.yaml:snakeyaml:1.25")
    testImplementation("junit:junit:4.12")
}

tasks {
    // run via ./gradlew test1 --console=plain --no-daemon
    register("test1", org.arl.fjage.gradle.FjageGroovyBootTask::class) {
        scripts = listOf("initrc-console-shell", "01_hello")
        systemProperties = mapOf("java.util.logging.config.file" to "logging.properties")
    }
}
```

## Tasks

### `packageFjage` - org.arl.fjage.gradle.FjagePackagingTask

* Depends on: `assemble`
* Makes a fjage package containing class files, dependencies and metadata.

### org.arl.fjage.gradle.FjageGroovyBootTask

* Depends on: `classes`
* Run scripts scripts via `org.arl.fjage.shell.GroovyBoot`.

#### Properties

| Name | Type | Description |
| --- | --- | --- |
| `scriptsDirectory` | `File`           | Directory containing script files. Defaults to `src/fjage/groovy`. |
| `scripts`          | `List<String>`   | Script names/locations (resolved against `scriptsDirectory`). |
| `classpath`        | `FileCollection` | Classpath. Defaults to `project.configurations.getByName("testRuntimeClasspath")`. |
| `systemProperties` | `Map<String, ?>` | System properties. |

## Lifecycle tasks

### `assemble`

* Depends on: `packageFjage`

## Project layout

### `src/fjage/groovy`

* fjage scripts.

## Dependency management

### Dependency configurations

#### `fjage`

* Dependencies provided by fjage.

#### `compileClasspath` additionally extends `fjage`

#### `testImplementation` additional extends `fjage`
