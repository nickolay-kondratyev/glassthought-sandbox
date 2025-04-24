package com.glassthought.sandbox

import com.glassthought.sandbox.demo.benchmark.ParsingBenchmarker
import kotlin.system.exitProcess

suspend fun main(args: Array<String>) {
  ParsingBenchmarker().go()
  exitProcess(0)
}
