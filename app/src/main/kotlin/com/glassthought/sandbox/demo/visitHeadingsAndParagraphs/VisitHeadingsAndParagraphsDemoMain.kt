package com.glassthought.sandbox.demo.visitHeadingsAndParagraphs

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
import kotlin.math.max // Import max function

val out = Out.standard()

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

  // --- Visitor Pattern for Headings and Paragraphs with Indentation ---
  println("\n--- Visiting Headings and Paragraphs (with Indentation) ---")

  var lastHeadingLevel = 0 // State: Tracks the level of the most recent heading encountered

  // Define a single visitor instance capable of handling multiple node types
  val contentVisitor = NodeVisitor(
    // Handler for Heading nodes
    VisitHandler(Heading::class.java) { heading ->
      val level = heading.level
      val textContent = heading.text.toString().trim()
      // Calculate indent: (level - 1) * 2 spaces
      val indent = "  ".repeat(max(0, level - 1)) // Ensure non-negative repeat count
      println("${indent}H$level: \"$textContent\"") // Use H notation for clarity
      lastHeadingLevel = level // Update the last seen heading level
    },

    // Handler for Paragraph nodes
    VisitHandler(Paragraph::class.java) { paragraph ->
      val textContent = paragraph.contentChars.toString().trim()
      if (textContent.isNotEmpty()) {
        // Calculate indent: (last heading level) * 2 spaces + 2 extra spaces
        val indent = "  ".repeat(max(0, lastHeadingLevel)) + "  " // Ensure non-negative repeat count
        println("${indent}P: \"$textContent\"") // Use P notation for clarity
      }
      // NOTE: This simple approach assumes paragraphs "belong" to the immediately preceding heading
      // in the traversal order. More complex structures might require tracking heading exits.
    }
  )

  // Start visiting the document from the root with the combined visitor
  contentVisitor.visit(document)

  println("--- Finished Visiting Headings and Paragraphs ---\n")
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
  // Using the specific note for this demo
  val testNote = projectDir.resolve("data/test-data/headings-and-paragraphs.md").verifyExists()
  return testNote
}
