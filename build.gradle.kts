plugins {
    application
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Local BIUOOP jar
    implementation(files("lib/biuoop-1.4.jar"))

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    // Use your current main first; we can switch to App later
    mainClass.set("com.yoad.arkanoid.Ass5Game")
}

tasks.test {
    useJUnitPlatform()
}
