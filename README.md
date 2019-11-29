# gradle-fjage-plugin

Gradle plugin for the fjage framework.

## Sample usage

### Kotlin DSL

```
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
