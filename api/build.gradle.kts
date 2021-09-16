import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.cadixdev.licenser")
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

    implementation("com.github.ajalt.colormath:colormath:3.0.0")

    implementation("org.danilopianini:java-quadtree:0.1.5-dev0u+fa95421")

}

configure<LicenseExtension> {
    header(rootProject.file("LICENSE"))
}

configure<JavaPluginExtension> {
    withSourcesJar()
}
