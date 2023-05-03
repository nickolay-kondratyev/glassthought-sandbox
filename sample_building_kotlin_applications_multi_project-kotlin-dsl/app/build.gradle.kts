
plugins {
    id("demo.kotlin-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation(project(":subDir:utilities"))
}

application {
    mainClass.set("demo.app.AppKt") // <1>
}
