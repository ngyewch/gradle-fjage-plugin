# gradle-fjage-plugin

Gradle plugin for the [fjåge](https://github.com/org-arl/fjage) framework.

![GitHub release (latest by date)](https://img.shields.io/github/v/release/ngyewch/gradle-fjage-plugin)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/ngyewch/gradle-fjage-plugin/Java%20CI)

## Sample usage

### Kotlin DSL

```kotlin
plugins {
    id("com.github.ngyewch.gradle-fjage-plugin") version "0.2.0"
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

fjage {
    // The following are defaults.
    mainSourceDirectory = file("src/main/fjage")
    testSourceDirectory = file("src/test/fjage")
    
    // Copy extra files.
    copyInto("misc", fileTree("src/main/fjage2") {
        exclude("**/*.log")
    })
}
```

### Groovy DSL

```groovy
plugins {
    id("com.github.ngyewch.gradle-fjage-plugin") version "0.2.0"
}

dependencies {
    "fjage"("com.github.org-arl:fjage:1.6.2")
    "fjage"("org.codehaus.groovy:groovy-all:2.5.8")
    implementation("org.yaml:snakeyaml:1.25")
    testImplementation("junit:junit:4.12")
}

// run via ./gradlew test1 --console=plain --no-daemon
tasks.register("test1", org.arl.fjage.gradle.FjageGroovyBootTask) {
    scripts = ["initrc-console-shell", "01_hello"]
    systemProperties = ["java.util.logging.config.file": "logging.properties"]
}
```

## Extension

fjage

### Properties

| Name | Type | Description |
| --- | --- | --- |
| `mainSourceDirectory` | `File` | Main fjage directory. Defaults to `src/main/fjage`. |
| `testSourceDirectory` | `File` | Test fjage directory. Defaults to `src/test/fjage`. |

### Methods

| Name | Type | Description |
| --- | --- | --- |
| `copyInto(String destination, FileTree fileTree)` | `void` | Copies the specified files into the specified destination directory in the fjage project. |
| `copyInto(FileTree fileTree)`                     | `void` | Copies the specified files into the base directory of the fjage project. |

## Tasks

### `packageFjage` - org.arl.fjage.gradle.FjagePackagingTask

* Depends on: `assemble`
* Makes a fjage package containing class files, dependencies and metadata.

### org.arl.fjage.gradle.FjageGroovyBootTask

* Depends on: `classes`
* Run scripts scripts via `org.arl.fjage.shell.GroovyBoot`.
* Working directory: `$buildDir/fjageGroovyBoot`

#### Properties

| Name | Type | Description |
| --- | --- | --- |
| `scripts`          | `List<String>`   | Script locations (resolved against `mainSourceDirectory` and `testSourceDirectory`). |
| `classpath`        | `FileCollection` | Classpath. Defaults to `sourceSets.test.runtimeClasspath`. |
| `systemProperties` | `Map<String, ?>` | System properties. |

## Lifecycle tasks

### `assemble`

* Depends on: `packageFjage`

## Project layout

Additional directories.

* src
    * main
        * fjage  `// fjage files for packaging/distribution`
    * test
        * fjage  `// fjage files for testing`

## Dependency management

### Dependency configurations

#### `fjage`

* Dependencies provided by fjage.

#### `compileClasspath` additionally extends `fjage`

#### `testImplementation` additional extends `fjage`
