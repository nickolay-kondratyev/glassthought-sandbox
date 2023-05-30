plugins {
    kotlin("multiplatform") version "1.8.0"
    kotlin("plugin.serialization") version "1.5.31"

    id("org.jetbrains.kotlinx.kover") version "0.7.0-Beta"

    // Add java plugin, should be used within the JVM code and not prevent us
    // writing code for other platforms.
    java

    // Add the maven-publish plugin to allow us to publish to maven local.
    id("maven-publish")
}

group = "dev.asgard"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(8)

        withJava()

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                // Co-routines support so that we perform long network operations
                // outside the main thread.
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("io.kotest:kotest-framework-engine:4.6.3")
                implementation("io.kotest:kotest-assertions-core:4.6.3")
                implementation("io.kotest:kotest-runner-junit5:4.6.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                // Place to add the JVM specific dependencies.
            }
        }
        val jvmTest by getting{
            dependencies {

                // Adding Kotest as the testing framework. More info in
                // http://localhost:8888/notes/i3fl4wmae0t35axabitiwvn/
                val kotestVersion = "4.6.3"
                implementation("io.kotest:kotest-framework-engine:$kotestVersion")
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }

    // Without this extra logging configurations the tests silently passed and the output
    // was not visible in the console.
    tasks.withType<Test> {
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            showExceptions = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showStackTraces = true
        }
    }
}
