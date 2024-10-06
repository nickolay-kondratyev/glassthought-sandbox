abstract class IncrementalReverseTask : DefaultTask() {
  @get:Incremental
  @get:PathSensitive(PathSensitivity.NAME_ONLY)
  @get:InputDirectory
  abstract val inputDir: DirectoryProperty

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @get:Input
  abstract val inputProperty: Property<String>

  @TaskAction
  fun execute(inputChanges: InputChanges) {
    println(
      if (inputChanges.isIncremental) {
        "Executing incrementally"
      } else {
        "Executing non-incrementally"
      }
    )

    inputChanges.getFileChanges(inputDir).forEach { change ->
      if (change.fileType == FileType.DIRECTORY) return@forEach

      println("${change.changeType}: ${change.normalizedPath}")
      val targetFile = outputDir.file(change.normalizedPath).get().asFile
      if (change.changeType == ChangeType.REMOVED) {
        targetFile.delete()
      } else {
        targetFile.writeText(change.file.readText().reversed())
      }
    }
  }
}

tasks.register<IncrementalReverseTask>("incrementalReverse") {
  inputDir = file("src/inputs")
  outputDir = layout.buildDirectory.dir("outputs")
  inputProperty = project.findProperty("taskInputProperty") as String? ?: "original"
}
