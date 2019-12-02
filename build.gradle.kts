plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    implementation("commons-io:commons-io:2.6")
    implementation("org.yaml:snakeyaml:1.25")
}

pluginBundle {
    website = "https://github.com/ngyewch/gradle-fjage-plugin"
    vcsUrl = "https://github.com/ngyewch/gradle-fjage-plugin.git"
    tags = listOf("gradle", "plugin", "fjage")
}

gradlePlugin {
    plugins {
        create("flagePlugin") {
            id = "com.github.ngyewch.gradle-fjage-plugin"
            displayName = "fjage Plugin"
            description = "Plugin for Gradle-based fjage development."
            implementationClass = "org.arl.fjage.gradle.FjagePlugin"
            version = "0.0.6"
        }
    }
}
