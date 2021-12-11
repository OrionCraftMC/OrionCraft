plugins {
    id("com.github.gmazzo.buildconfig") version "3.0.3"
}

dependencies {
    // Align versions of all Kotlin components
    api(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("org.jetbrains.kotlin:kotlin-reflect")

    api("com.google.guava:guava:14.0.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    api("com.github.ajalt.colormath:colormath:3.0.0")

    // Json I/O
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")

    /* Discord SDK Integration */
    implementation("com.github.JnCrMx:discord-game-sdk4j:2b5a903204")

    /* HTTP I/O */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

    /* Meditate Layout */
    implementation("io.github.orioncraftmc:meditate-layout:1.0.0")

    /* LibNinePatch */
    implementation("io.github.juuxel:libninepatch:1.1.0")
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by rootProject.extra
val details = versionDetails()

buildConfig {
    className("BuildConstants")
    buildConfigField("String", "COMMIT_HASH", "\"${details.gitHash}\"")
    buildConfigField("String", "COMMIT_BRANCH", "\"${details.branchName}\"")
}
