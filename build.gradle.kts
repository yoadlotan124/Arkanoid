plugins {
    application
    java
}

java {
    // use your installed JDK, but compile/target Java 17 bytecode
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("lib/biuoop-1.4.jar"))

    // JUnit 5 + launcher so Gradle test executor can boot
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
}

application {
    mainClass.set("com.yoad.arkanoid.App")
    applicationDefaultJvmArgs = listOf("-Xms128m", "-Xmx512m")
    applicationName = "arkanoid"
}

tasks.test {
    useJUnitPlatform()
}

// ensure correct stdlib targeting with newer JDKs
tasks.withType(JavaCompile::class) {
    options.release.set(17)
}
