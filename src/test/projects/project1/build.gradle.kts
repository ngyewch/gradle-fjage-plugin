plugins {
    java
    groovy
    id("com.github.ngyewch.gradle-fjage-plugin")
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.yaml:snakeyaml:1.25")
    "fjage"("com.github.org-arl:fjage:1.6.2")
    "fjage"("org.codehaus.groovy:groovy-all:2.5.8")
    testImplementation("junit:junit:4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    register("test1", org.arl.fjage.gradle.FjageGroovyBootTask::class) {
        scripts = listOf("etc/initrc-console-shell", "scripts/01_hello")
        systemProperties = mapOf("java.util.logging.config.file" to "logging.properties")
    }
}

fjage {
    mainSourceDirectory = file("src/main/fjage")
    testSourceDirectory = file("src/test/fjage")
    copyInto("misc", fileTree("src/main/fjage2") {
        exclude("**/*.log")
    })
}
