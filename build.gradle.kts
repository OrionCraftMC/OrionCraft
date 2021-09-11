plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.30" apply false
    id("org.cadixdev.licenser") version "0.6.1" apply false
}

subprojects {
    apply(plugin = "org.gradle.java-library")
    apply(plugin = "org.gradle.maven-publish")

    group = "io.github.orioncraftmc.orion"
    version = "1.0-SNAPSHOT"

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

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_16.toString()
        }
    }
}
