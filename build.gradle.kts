import org.gradle.api.tasks.compile.JavaCompile

plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType(JavaCompile::class) {
    options.release.set(17)
}

dependencies {
    // ---- JUnit 5 (Gradle 9 friendly) ----
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
}

javafx {
    version = "21.0.5"
    modules = listOf(
        "javafx.controls",
        "javafx.graphics",
        "javafx.media"
    )
}

application {
    mainClass.set("com.yoad.arkanoid.App")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("FAILED", "SKIPPED")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

// JavaFX plugin's run touches SourceSetContainer; disable config cache for it
tasks.named<org.gradle.api.tasks.JavaExec>("run") {
    notCompatibleWithConfigurationCache("JavaFX run task touches SourceSetContainer via plugin")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).encoding = "UTF-8"
}
