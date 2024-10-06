import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.InputChanges

plugins {
  base
}

abstract class ProcessFilesTask : DefaultTask() {

  @get:InputDirectory
  abstract val inputDir: DirectoryProperty

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @TaskAction
  fun process(inputChanges: InputChanges) {
    if (!inputDir.isPresent) {
      throw IllegalStateException("inputDir is not set for task: [$name]")
    }

    if (!outputDir.isPresent) {
      throw IllegalStateException("outputDir is not set for task: [$name]")
    }

    processImpl(inputChanges)
  }

  private fun processImpl(inputChanges: InputChanges) {
    // Ensure the output directory exists
    outputDir.get().asFile.mkdirs()

    // Get the list of changed files from inputDir
    val changedFiles = inputChanges.getFileChanges(inputDir)

    if (!changedFiles.iterator().hasNext()) {
      println("No changes detected. Nothing to process.")
      return
    }

    changedFiles.forEach { change ->
      val inputFile = change.file
      val outputFile = outputDir.get().file(inputDir.get().asFile.toPath().relativize(inputFile.toPath()).toString()).asFile

      when (change.changeType) {
        ChangeType.ADDED, ChangeType.MODIFIED -> {
          if (inputFile.isFile) {
            // Read content, process it, and write to the output file
            val content = inputFile.readText().toUpperCase()
            outputFile.parentFile.mkdirs() // Ensure parent directories exist
            outputFile.writeText(content)
            println("Processed file: ${inputFile.name}")
          }
        }

        ChangeType.REMOVED -> {
          if (outputFile.exists()) {
            outputFile.delete()
            println("Deleted output file for removed input file: ${inputFile.name}")
          }
        }
      }
    }
  }
}

// Register the task
tasks.register<ProcessFilesTask>("processFiles") {
  inputDir.set(layout.projectDirectory.dir("src/input"))
  outputDir.set(layout.buildDirectory.dir("output"))
}
