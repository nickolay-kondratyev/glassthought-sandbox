tasks.register("hello") {
    doLast {
        println("Hello world!")
    }
}
tasks.register("task-depends-on-hello") {
    dependsOn("hello")
    doLast {
        println("I'm Gradle")
    }
}
