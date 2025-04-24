package com.glassthought.sandbox.demo.full

import com.asgard.core.file.Directory
import com.asgard.core.file.File
import com.asgard.core.util.envDirectory
import com.glassthought.sandbox.FlexmarkUtils
import com.glassthought.sandbox.render.renderHtml
import com.glassthought.sandbox.util.out.Out
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ast.* // Import core AST nodes like Heading, Link, Image, Paragraph, Text, AutoLink
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler
import java.lang.StringBuilder // Already imported by default

// Use standard output primarily for reporting file locations now.
val out = Out.standard()

/**
 * Main entry point for the application.
 * Reads a markdown file, parses it, extracts various elements,
 * and writes the extracted information to separate files in the .out directory.
 */
fun main() {
  val projectDir = envDirectory("GLASSTHOUGHT_SANDBOX")

  // Ensure the output directory exists and is clean
  val outDir = projectDir.resolveDirectory(".out").mkdirs()
  outDir.listFilesRecursively().forEach { it.delete() }

  val testNote = projectDir.resolve("data/test-data/test-note-1.md").verifyExists()

  // Log the input file being processed
  out.println("Reading test note file: ${testNote.path}")
  val markdownContent = testNote.readText()

  val options = FlexmarkUtils.standardOptions()
  val parser = Parser.builder(options).build()

  // Parse document
  val document = parser.parse(markdownContent)

  // --- File Outputs ---
  val outputFiles = mutableListOf<File>()

  outputFiles.add(processFrontMatter(document, outDir))
  outputFiles.add(extractAndWriteHeadings(document, outDir))
  outputFiles.add(extractAndWriteLinks(document, outDir))
  outputFiles.add(extractAndWriteImages(document, outDir))
  outputFiles.add(extractAndWriteCodeBlocks(document, outDir))
  outputFiles.add(extractAndWriteParagraphsPreview(document, outDir))
  outputFiles.add(writeDocumentStructure(document, outDir))
  outputFiles.add(renderHtml(document, options, outDir))
  outputFiles.add(generateWorkflowSummary(document, outDir)) // Pass document and outDir

  // Print the locations of all generated files
  out.println("\n--- Output Files ---")
  outputFiles.forEach { file ->
    out.println("Output written to: ${file.absoluteNormalizedPathStr}")
  }
  out.println("Processing complete.")
}

/**
 * Processes the YAML front matter from the document and writes it to a file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun processFrontMatter(document: Document, outDir: Directory): File {
  val frontMatterFile = outDir.resolve("front_matter.yaml")
  val frontMatterVisitor = AbstractYamlFrontMatterVisitor()
  frontMatterVisitor.visit(document)
  val frontMatterContent = buildString {
    frontMatterVisitor.data.forEach { key, values ->
      appendLine("$key: ${values.joinToString(", ")}")
    }
  }
  // Ensure writing empty file if no front matter exists
  frontMatterFile.writeText(frontMatterContent)
  return frontMatterFile
}

/**
 * Extracts headings from the document and writes them to a markdown file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun extractAndWriteHeadings(document: Document, outDir: Directory): File {
  val headingsFile = outDir.resolve("headings.md")
  val headings = collectHeadings(document)
  val headingsContent = buildString {
    appendLine("# Headings")
    headings.forEach { (level, text) ->
      appendLine("${"#".repeat(level)} $text") // Markdown format
    }
  }
  headingsFile.writeText(headingsContent)
  return headingsFile
}

/**
 * Extracts links from the document and writes them to a markdown file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun extractAndWriteLinks(document: Document, outDir: Directory): File {
  val linksFile = outDir.resolve("links.md")

  val links = collectLinks(document)
  val linksContent = buildString {
    appendLine("# Links")
    links.forEach { (text, url) ->
      appendLine("- [$text]($url)")
    }
  }
  linksFile.writeText(linksContent)
  return linksFile
}

/**
 * Extracts images from the document and writes them to a markdown file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun extractAndWriteImages(document: Document, outDir: Directory): File {
  val imagesFile = outDir.resolve("images.md")
  val images = collectImages(document)
  val imagesContent = buildString {
    appendLine("# Images")
    images.forEach { (alt, url) ->
      appendLine("![$alt]($url)") // Markdown image format
    }
  }
  imagesFile.writeText(imagesContent)
  return imagesFile
}

/**
 * Extracts code blocks from the document and writes them to a text file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun extractAndWriteCodeBlocks(document: Document, outDir: Directory): File {
  val codeBlocksFile = outDir.resolve("code_blocks.txt")
  val codeBlocks = FlexmarkUtils.extractCodeBlocks(document)
  val codeBlocksContent = buildString {
    appendLine("--- Code Blocks ---")
    codeBlocks.forEach { block ->
      appendLine("Language: ${block.language ?: "N/A"}") // Provide N/A if language is null
      appendLine("```${block.language ?: ""}") // Empty ``` for no language
      appendLine(block.content.trimEnd()) // Use trimEnd to avoid extra newline
      appendLine("```")
      appendLine() // Add a blank line between blocks
    }
  }
  codeBlocksFile.writeText(codeBlocksContent)
  return codeBlocksFile
}

/**
 * Extracts a preview of paragraphs (first 3) from the document and writes them to a text file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun extractAndWriteParagraphsPreview(document: Document, outDir: Directory): File {
  val paragraphsFile = outDir.resolve("paragraphs_preview.txt")
  val paragraphs = FlexmarkUtils.extractParagraphs(document)
  val paragraphsContent = buildString {
    appendLine("--- Paragraphs (first 3) ---")
    if (paragraphs.isNotEmpty()) {
      paragraphs.take(3).forEachIndexed { index, paragraph ->
        appendLine("Paragraph ${index + 1}:")
        appendLine(paragraph)
        if (index < 2 && index < paragraphs.size - 1) { // Add separator only between paragraphs
          appendLine("---")
        }
      }
    } else {
      appendLine("(No paragraphs found)")
    }
  }
  paragraphsFile.writeText(paragraphsContent)
  return paragraphsFile
}

/**
 * Generates a representation of the document structure (limited depth) and writes it to a text file.
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
private fun writeDocumentStructure(document: Document, outDir: Directory): File {
  val structureFile = outDir.resolve("document_structure.txt")
  val structureContent = buildString {
    appendLine("--- Document Structure (limited depth) ---")
    appendDocumentStructure(document, this)
  }
  structureFile.writeText(structureContent)
  return structureFile
}


/**
 * Demonstrates a workflow for processing markdown content and writes the output to a file.
 *
 * @param document The parsed markdown document.
 * @param outDir The directory to write the output file to.
 * @return The generated File object containing the workflow summary.
 */
private fun generateWorkflowSummary(document: Document, outDir: Directory): File {
  val outputFile = outDir.resolve("workflow_summary.txt")

  // Extract title from the front matter
  val frontMatterVisitor = AbstractYamlFrontMatterVisitor()
  frontMatterVisitor.visit(document)
  val title = frontMatterVisitor.data["title"]?.firstOrNull() ?: "Untitled"

  // Get headings and create a table of contents
  val headings = collectHeadings(document)

  // Extract the first paragraph as a summary
  val firstParagraph = FlexmarkUtils.extractParagraphs(document).firstOrNull() ?: ""

  // Count various elements
  val links = collectLinks(document)
  val images = collectImages(document)
  val codeBlocks = FlexmarkUtils.extractCodeBlocks(document)
  val paragraphsCount = FlexmarkUtils.extractParagraphs(document).size

  // Build the output string
  val workflowContent = buildString {
    appendLine("--- Workflow Example: Extract and Process Document ---")
    appendLine("Document Title: $title")

    appendLine("\nTable of Contents:")
    if (headings.isNotEmpty()) {
      headings.forEach { (level, text) ->
        appendLine("${"  ".repeat(level)}- $text") // Indent based on level
      }
    } else {
      appendLine("(No headings found)")
    }

    if (firstParagraph.isNotEmpty()) {
      appendLine("\nSummary (First Paragraph):")
      appendLine(firstParagraph)
    } else {
      appendLine("\nSummary (First Paragraph):")
      appendLine("(No paragraphs found)")
    }

    appendLine("\nDocument Statistics:")
    appendLine("- Headings: ${headings.size}")
    appendLine("- Links: ${links.size}")
    appendLine("- Images: ${images.size}")
    appendLine("- Code Blocks: ${codeBlocks.size}")
    appendLine("- Paragraphs: $paragraphsCount")
  }

  // Write the content to the specified file
  outputFile.writeText(workflowContent)
  return outputFile // Return the created file
}


/**
 * Collects all headings from the document with their levels.
 */
fun collectHeadings(document: Node): List<Pair<Int, String>> {
  val headings = mutableListOf<Pair<Int, String>>()
  val visitor = NodeVisitor(
    VisitHandler(Heading::class.java) { heading ->
      // Use heading.text which represents the parsed content without the '#' characters
      headings.add(heading.level to heading.text.unescape())
    }
  )
  visitor.visit(document)
  return headings
}

/**
 * Collects all links from the document.
 */
fun collectLinks(document: Node): List<Pair<String, String>> {
  val links = mutableListOf<Pair<String, String>>()

  val visitor = NodeVisitor(
    VisitHandler(Link::class.java) { link ->
      // Use link.text for the link text and link.url for the URL
      links.add(link.text.unescape() to link.url.unescape())
    },
    // Also consider AutoLink
    VisitHandler(AutoLink::class.java) { link ->
      links.add(link.text.unescape() to link.url.unescape())
    }
    // Add handlers for other link types like LinkRef if needed
  )

  visitor.visit(document)
  return links
}

/**
 * Collects all images from the document.
 */
fun collectImages(document: Node): List<Pair<String, String>> {
  val images = mutableListOf<Pair<String, String>>()
  val visitor = NodeVisitor(
    VisitHandler(Image::class.java) { image ->
      // Use image.text for alt text and image.url for the image source URL
      images.add(image.text.unescape() to image.url.unescape())
    }
    // Add handlers for ImageRef if needed
  )
  visitor.visit(document)
  return images
}

/**
 * Appends the structure of the document to a StringBuilder for demonstration purposes,
 * limiting the recursion depth.
 *
 * @param node The current node to process.
 * @param stringBuilder The StringBuilder to append the structure to.
 * @param level The current indentation level.
 * @param maxLevel The maximum recursion depth.
 */
fun appendDocumentStructure(
  node: Node,
  stringBuilder: StringBuilder,
  level: Int = 0,
  maxLevel: Int = 3 // Keep the depth limit
) {
  if (level > maxLevel) return // Limit depth

  val indent = "  ".repeat(level) // Two spaces indent
  // Include node text snippet if available and simple (like for Text nodes)
  val nodeTextSnippet = if (node is Text) ": \"${node.chars.unescape().take(30)}...\"" else ""
  stringBuilder.appendLine("$indent- ${node.javaClass.simpleName}$nodeTextSnippet")

  // Recursively process child nodes
  var currentChild: Node? = node.firstChild
  while (currentChild != null) {
    appendDocumentStructure(currentChild, stringBuilder, level + 1, maxLevel)
    currentChild = currentChild.next
  }
}
