import org.gradle.api.tasks.JavaExec

plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType(JavaCompile::class) { options.release.set(17) }

repositories { mavenCentral() }

dependencies {
    // JUnit (unchanged)
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
}

javafx {
    version = "17.0.16"          // pairs with Java 17 (LTS)
    modules("javafx.controls", "javafx.graphics")
}

application {
    mainClass.set("com.yoad.arkanoid.App")
}

tasks.test { 
    useJUnitPlatform() 
}

tasks.named<JavaExec>("run") {
    notCompatibleWithConfigurationCache("JavaFX run task touches SourceSetContainer via plugin")
}

