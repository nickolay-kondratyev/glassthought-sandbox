package com.glassthought.sandbox

import com.asgard.core.file.directory
import gt.sandbox.util.output.Out

/**
 * Main entry point for the application.
 */
fun main() {
  val out = Out.standard()


  out.println("Hello, asgard dependency! Number of files in tmp dir: ${directory("/tmp").listFiles().size}")
}
