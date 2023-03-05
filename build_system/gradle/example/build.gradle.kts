tasks.register<Exec>("runScript") {

    commandLine("bash", "./example.sh")

    workingDir = File("../../some_dir")
}
