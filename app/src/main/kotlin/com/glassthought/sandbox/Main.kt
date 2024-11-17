package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings
import kotlinx.coroutines.*

val out = Out.standard(OutSettings(printCoroutineName = false))

fun main() = runBlocking {
  sequenceOf(1, 2, 3)
    .filter { out.printBlue("F:$it, "); it % 2 == 1 }
    .map { out.printGreen("M:$it, "); it.toString() + "a"}
    .forEach { out.print("E:$it, ") }
}
