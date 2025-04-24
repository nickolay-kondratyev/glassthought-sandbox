package com.glassthought.sandbox.demo.visitHeadings

import com.asgard.core.file.Directory
import com.asgard.core.file.File
import com.asgard.core.util.envDirectory
import com.glassthought.sandbox.FlexmarkUtils
// Removed unused imports for brevity
import com.glassthought.sandbox.util.out.Out
import com.vladsch.flexmark.parser.Parser
// Removed unused import: com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ast.* // Import core AST nodes like Heading, Link, Image, Paragraph, Text, AutoLink
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler

// Removed unused import: import java.lang.StringBuilder

// Use standard output primarily for reporting file locations now.
val out = Out.standard()

/**
 * Main entry point for the application.
 * Reads a markdown file, parses it, extracts various elements,
 * and writes the extracted information to separate files in the .out directory.
 */
fun main() {
  val projectDir = envDirectory("GLASSTHOUGHT_SANDBOX")
  val outDir = getOutDir(projectDir)
  val testNote = getTestNote(projectDir)

  outDir.resolve("original.md").writeText(testNote.readText())

  // Log the input file being processed
  out.println("Reading test note file: ${testNote.path}")
  val markdownContent = testNote.readText()

  val options = FlexmarkUtils.standardOptions()
  val parser = Parser.builder(options).build()

  // Parse document
  val document = parser.parse(markdownContent)

  // --- Add Visitor Pattern for Headings ---
  println("\n--- Visiting Headings ---")

  // Define the visitor logic for Heading nodes
  val headingVisitor = NodeVisitor(
    VisitHandler(Heading::class.java) { heading ->
      // 'it' or 'heading' is the Heading node instance
      val level = heading.level

      // heading.text gets the text part of the heading (without the '#') as a BasedSequence
      val textContent = heading.text.toString().trim() // Convert BasedSequence to String and trim whitespace
      println("  Level $level Heading: \"$textContent\"")
      // We don't need to visit children of a heading for this task,
      // but if we did, we would call:
      // headingVisitor.visitChildren(heading)
    }
    // You could add more VisitHandlers here for other node types
  )

  // Start visiting the document from the root
  headingVisitor.visit(document)

  println("--- Finished Visiting Headings ---\n")
  // --- End of Visitor Pattern section ---


  // You can add other processing steps here if needed

}

private fun getOutDir(projectDir: Directory): Directory {
  // Ensure the output directory exists and is clean
  val outDir = projectDir.resolveDirectory(".out").mkdirs()
  outDir.listFilesRecursively().forEach { it.delete() }
  return outDir
}

private fun getTestNote(projectDir: Directory): File {
  // Using the heading-focused note provided in the previous context
  val testNote = projectDir.resolve("data/test-data/simple-note-heading-focused.md").verifyExists()
  return testNote
}
