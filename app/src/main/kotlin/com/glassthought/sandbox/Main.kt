package com.glassthought.sandbox

import com.asgard.core.util.envDirectory
import gt.sandbox.util.output.Out

val out = Out.standard()

/**
 * Main entry point for the application.
 */
fun main() {
  val projectDir = envDirectory("GLASSTHOUGHT_SANDBOX")

  val testNote = projectDir.resolve("data/test-data/test-note-1.md").verifyExists()


  out.println(testNote.readText())
}
