plugins {
    kotlin("multiplatform") version "1.6.0"
}

group = "com.example"
version = "1.0.0"

kotlin {
    /* Targets */
    repositories {
        mavenCentral()
    }

    jvm()
    ios()

    /* Source Sets */
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val iosMain by getting
        val iosTest by getting
    }
}
