package gt.kotlin.sandbox.main

import gt.kotlin.sandbox.internal.output.Out

fun main() {
    val out = Out.standard()
    out.printlnGreen("Hello World!")
    out.printInBlue("hi")
    out.printInRed("hi")
}
