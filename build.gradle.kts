plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories { mavenCentral() }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
}

javafx {
    version = "21.0.5"
    modules = listOf("javafx.controls", "javafx.graphics", "javafx.media")
}

application {
    mainClass.set("com.yoad.arkanoid.App")
}

tasks.test { useJUnitPlatform() }

tasks.named<org.gradle.api.tasks.JavaExec>("run") {
    notCompatibleWithConfigurationCache("JavaFX run task touches SourceSetContainer")
}
