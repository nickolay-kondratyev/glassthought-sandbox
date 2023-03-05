tasks.register<Exec>("runScript") {
    commandLine("sh", "./example.sh")

    workingDir = File("../../some_dir")
}
