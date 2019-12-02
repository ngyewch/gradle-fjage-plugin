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
        scripts = listOf("initrc-console-shell", "01_hello")
        systemProperties = mapOf("java.util.logging.config.file" to "logging.properties")
    }
}

/*
afterEvaluate {
    val fjageConfiguration:Configuration = configurations.getByName("fjage");
    val compileClasspathConfiguration:Configuration = configurations.getByName("compileClasspath");
    val testImplementationConfiguration:Configuration = configurations.getByName("testImplementation");

    compileClasspathConfiguration.extendsFrom(fjageConfiguration)
    testImplementationConfiguration.extendsFrom(fjageConfiguration)
}
*/