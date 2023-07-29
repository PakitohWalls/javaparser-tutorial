plugins {
    id("java")
}

group = "com.autentia.tutorials"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.javaparser:javaparser-core:3.28.1")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}