package com.glassthought.sandbox.render

import com.asgard.core.file.Directory
import com.asgard.core.file.File
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.data.DataHolder

/**
 * Renders the document to HTML and writes it to a preview file.
 * @param document The parsed markdown document.
 * @param options The Flexmark options used for parsing/rendering.
 * @param outDir The directory to write the output file to.
 * @return The generated File object.
 */
fun renderHtml(
  document: Document,
  options: DataHolder,
  outDir: Directory
): File {
  val htmlPreviewFile = outDir.resolve("html_preview.html")
  val renderer = HtmlRenderer.builder(options).build()
  val html = renderer.render(document)
  val htmlPreviewContent = buildString {
    appendLine("<!DOCTYPE html>") // Use standard DOCTYPE
    appendLine("<html>")
    appendLine("<head>")
    appendLine("  <meta charset=\"UTF-8\">") // Specify charset
    appendLine("  <title>Preview</title>")
    appendLine("</head>")
    appendLine("<body>")
    appendLine(html) // Include the rendered HTML content
    appendLine("</body>") // Ensure closing tags
    appendLine("</html>")
  }
  htmlPreviewFile.writeText(htmlPreviewContent)
  return htmlPreviewFile
}
