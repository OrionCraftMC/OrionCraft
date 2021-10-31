plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.30" apply false
    id("org.cadixdev.licenser") version "0.6.1" apply false
}

subprojects {
    apply(plugin = "org.gradle.java-library")
    apply(plugin = "org.gradle.maven-publish")

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.cadixdev.licenser")

    group = "io.github.orioncraftmc.orion"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    tasks {
        extensions.getByType(JavaPluginExtension::class.java).apply {
            targetCompatibility = JavaVersion.VERSION_16
            sourceCompatibility = JavaVersion.VERSION_16
        }
    }

    extensions.getByType<PublishingExtension>().apply {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }

    configure<org.cadixdev.gradle.licenser.LicenseExtension> {
        header(rootProject.file("LICENSE"))
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
    }


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_16.toString()
        }
    }
}
