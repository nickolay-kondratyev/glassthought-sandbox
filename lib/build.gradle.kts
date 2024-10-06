import java.time.Instant

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.register("hello") {
    println("Configuration of 'hello' task")

    doLast {
        println("Execution of 'hello': Hello, Gradle!")
    }
}

tasks.register("myTask") {
    // Define a custom task property
    val greeting = project.objects.property(String::class.java)

    // Configure the property during the Configuration phase
    greeting.set("Hello, Gradle!")

    doLast {
        // Access and use the property during the Execution phase
        println(greeting.get())
    }
}

tasks.register("saveEnvVarToFile") {
    val envVar = System.getenv("MY_ENV_VAR")

    // Without declaring the property as input and relying on env variable.
    // inputs.property("MY_ENV_VAR", envVar)
    outputs.file("build/output.txt")    // Output file

    doLast {
        val outputFile = file("build/output.txt")
        outputFile.writeText("Environment property: $envVar \n")

        println("Environment variable: $envVar")
    }
}
