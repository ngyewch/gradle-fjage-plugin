plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
    jcenter()
}

tasks {
    register("testZip", Zip::class) {
        from("src/test/projects") {
            exclude("**/.gradle/**")
        }
        archiveFileName.set("test.zip")
        destinationDirectory.set(project.file("$buildDir/testZip"))
    }
    named("processTestResources") {
        dependsOn("testZip")
    }

    register("createClasspathManifest") {
        val outputDir = file("$buildDir/$name")

        inputs.files(sourceSets.main.get().runtimeClasspath)
                .withPropertyName("runtimeClasspath")
                .withNormalizer(ClasspathNormalizer::class)
        outputs.dir(outputDir)
                .withPropertyName("outputDir")

        doLast {
            outputDir.mkdirs()
            file("$outputDir/plugin-classpath.txt")
                    .writeText(sourceSets.main.get().runtimeClasspath.joinToString("\n"))
        }
    }
}

sourceSets {
    test {
        resources {
            setSrcDirs(listOf("src/test/resources", "$buildDir/testZip"))
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    implementation("commons-io:commons-io:2.6")
    implementation("org.yaml:snakeyaml:1.25")

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testRuntimeOnly(files(tasks["createClasspathManifest"]))
}

pluginBundle {
    website = "https://github.com/ngyewch/gradle-fjage-plugin"
    vcsUrl = "https://github.com/ngyewch/gradle-fjage-plugin.git"
    tags = listOf("fjage")
}

gradlePlugin {
    plugins {
        create("flagePlugin") {
            id = "com.github.ngyewch.gradle-fjage-plugin"
            displayName = "fjage Plugin"
            description = "Plugin for Gradle-based fjage development."
            implementationClass = "org.arl.fjage.gradle.FjagePlugin"
            version = "0.0.8"
        }
    }
}
