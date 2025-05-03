package com.glassthought.sandbox.demo.format

import com.glassthought.sandbox.FlexmarkUtils
import com.vladsch.flexmark.formatter.Formatter
import com.vladsch.flexmark.parser.Parser

class FormatListsDemoMain {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val options = FlexmarkUtils.standardOptions()
      val parser = Parser.builder(options).build()
      val formatter = Formatter.builder(options).build()

      val input = """
- top
    - nested 1
    - nested 2
        - nested 2.1
        - nested 2.2
      """
      val parsed = parser.parse(
        input.trimIndent()
      )

      val formatted = formatter.render(parsed)

      println("### Input")
      println(input)

      println("### Parsed")
      println(formatted)


    }
  }
}
