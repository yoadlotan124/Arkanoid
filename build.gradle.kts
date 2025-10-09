import org.gradle.api.tasks.JavaExec

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType(JavaCompile::class) { options.release.set(17) }

repositories { mavenCentral() }

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

javafx {
    version = "21.0.5"
    modules = listOf("javafx.controls", "javafx.graphics", "javafx.media")
}


application {
    mainClass.set("com.yoad.arkanoid.App") // or FxLauncher if you prefer
}

tasks.test { 
    useJUnitPlatform() 
}

tasks.named<JavaExec>("run") {
    notCompatibleWithConfigurationCache("JavaFX run task touches SourceSetContainer via plugin")
}

