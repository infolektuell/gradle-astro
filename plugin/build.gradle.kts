plugins {
    kotlin("jvm") version "2.0.20"
    signing
    id("com.gradle.plugin-publish") version "1.2.2"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(22)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    website = "https://github.com/infolektuell/gradle-astro"
    vcsUrl = "https://github.com/infolektuell/gradle-astro.git"
    val astro by plugins.creating {
        id = "de.infolektuell.astro"
        displayName = "Astro Plugin"
        description = "Builds and checks Astro sites"
        tags = listOf("Astro", "SSG", "web")
        implementationClass = "de.infolektuell.gradle.astro.AstroPlugin"
    }
}

signing {
    // Get credentials from env variables for better CI compatibility
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}
