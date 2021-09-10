import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
    id("org.cadixdev.licenser") version "0.6.1"

    `java-library`
    `maven-publish`
}

group = "io.github.orioncraftmc.orion"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget  = JavaVersion.VERSION_16.toString()
    }
}


repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api("com.google.guava:guava:14.0.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

}

configure<LicenseExtension> {
    header(rootProject.file("LICENSE"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
