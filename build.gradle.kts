plugins {
    application
    java
}

java {
    // compile for Java 17 features/bytecode while using your current JDK
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("lib/biuoop-1.4.jar"))

    // JUnit 5 + launcher (needed by Gradle’s test executor)
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
}

application {
    mainClass.set("com.yoad.arkanoid.App")
}

tasks.test {
    useJUnitPlatform()
}

// ensure correct stdlib targeting
tasks.withType(JavaCompile::class) {
    options.release.set(17)
}
