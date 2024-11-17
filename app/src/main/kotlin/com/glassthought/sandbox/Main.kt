package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings

val out = Out.standard(OutSettings(printCoroutineName = false))

fun <T> T.printlnGreen() {
  out.printGreen(this.toString())
  println()
}

fun main(args: Array<String>) {

  println("As list - finding a match:")

  (1..10)
    .filter { out.printBlue("F$it, "); it % 2 == 1 }
    .map { print("M$it, "); it * 2 }
    .find { it > 5 }
    .printlnGreen()

  println()
  println()
  println("As sequence - finding a match:")
  (1..10).asSequence()
    .filter { out.printBlue("F$it, "); it % 2 == 1 }
    .map { print("M$it, "); it * 2 }
    .find { it > 5 }
    .printlnGreen()
}
