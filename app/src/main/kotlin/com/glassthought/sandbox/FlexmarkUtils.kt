package com.glassthought.sandbox

import com.vladsch.flexmark.ast.* // Import core AST nodes like Heading, Link, Image, Paragraph, Text, etc.
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension // Keep useful extensions
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.PegdownExtensions
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler
import com.vladsch.flexmark.util.data.DataHolder
import com.vladsch.flexmark.util.data.MutableDataSet
import java.lang.StringBuilder // Use java.lang.StringBuilder

/**
 * Utility object for lexmark parsing and element extraction.
 * Reconciled from two previous versions.
 */
object FlexmarkUtils {

  /**
   * Data class to hold code block information.
   * Uses nullable language as indented code blocks don't have language specifiers.
   */
  data class CodeBlockInfo(val language: String?, val content: String)

  /**
   * Creates a standard Flexmark parser with common extensions enabled.
   *
   * Includes Tables, Footnotes, and YAML Front Matter.
   */
  fun createStandardParser(): Parser {
    return Parser.builder(standardOptions()).build()
  }

  fun standardOptions(): DataHolder {
    return MutableDataSet().apply {
      set(
        Parser.EXTENSIONS, listOf(
          WikiLinkExtension.create(),
          TablesExtension.create(),
          FootnoteExtension.create(), // Included from one version
          YamlFrontMatterExtension.create()
          // Add other extensions here if needed later
        )
      )
    }
  }

  /**
   * Extracts all code blocks (both fenced and indented) from the document.
   * Uses the more robust implementation handling both types and nullable language.
   */
  fun extractCodeBlocks(document: Node): List<CodeBlockInfo> {
    val blocks = mutableListOf<CodeBlockInfo>()
    val visitor = NodeVisitor(
      // Handler for ``` fenced code blocks ```
      VisitHandler(FencedCodeBlock::class.java) { block ->
        // block.info contains the language string after the opening ```
        // Use unescape() for potential entities and takeIf for blank check
        val language = block.info.unescape().takeIf { it.isNotBlank() }
        // Use normalizeEOL to handle mixed line endings consistently
        val content = block.contentChars.normalizeEOL()
        blocks.add(CodeBlockInfo(language, content))
      },
      // Handler for indented code blocks (4 spaces)
      VisitHandler(IndentedCodeBlock::class.java) { block ->
        // Indented code blocks don't have language info in standard Markdown
        val content = block.contentChars.normalizeEOL()
        blocks.add(CodeBlockInfo(language = null, content = content)) // Language is null
      }
    )
    visitor.visit(document)
    return blocks
  }

  /**
   * Extracts the text content of all paragraph nodes.
   * Uses the implementation that traverses children for accurate text content.
   */
  fun extractParagraphs(document: Node): List<String> {
    val paragraphs = mutableListOf<String>()
    val visitor = NodeVisitor(
      VisitHandler(Paragraph::class.java) { paragraph ->
        // Use a nested visitor to accurately collect text from children
        val paragraphText = StringBuilder()
        val textVisitor = NodeVisitor(
          VisitHandler(Text::class.java) { textNode ->
            paragraphText.append(textNode.chars.unescape())
          },
          VisitHandler(SoftLineBreak::class.java) { _ ->
            paragraphText.append(' ') // Treat soft breaks as space for continuous text
          },
          VisitHandler(HardLineBreak::class.java) { _ ->
            paragraphText.append('\n') // Treat hard breaks as newline
          },
          // Add handlers for other inline elements if needed (e.g., Code, Emphasis)
          VisitHandler(Code::class.java) { code ->
            paragraphText.append(code.text.unescape())
          },
          VisitHandler(Emphasis::class.java) { emphasis ->
            // Recursively visit children of emphasis node
            visitChildrenText(emphasis, paragraphText)
          },
          VisitHandler(StrongEmphasis::class.java) { strong ->
            visitChildrenText(strong, paragraphText)
          }
          // Add more inline handlers as required...
        )
        textVisitor.visitChildren(paragraph) // Visit only children of the paragraph

        if (paragraphText.isNotEmpty()) {
          paragraphs.add(paragraphText.toString())
        }
      }
    )
    visitor.visit(document)
    return paragraphs
  }

  /** Helper to recursively get text from children of inline formatting nodes */
  private fun visitChildrenText(node: Node, builder: StringBuilder) {
    val textVisitor = NodeVisitor(
      VisitHandler(Text::class.java) { textNode ->
        builder.append(textNode.chars.unescape())
      },
      VisitHandler(SoftLineBreak::class.java) { _ -> builder.append(' ') },
      VisitHandler(HardLineBreak::class.java) { _ -> builder.append('\n') },
      VisitHandler(Code::class.java) { code -> builder.append(code.text.unescape()) },
      // Avoid infinite recursion by not re-handling Emphasis/StrongEmphasis here
      // Handle other nested inlines if necessary
    )
    textVisitor.visitChildren(node)
  }
}
