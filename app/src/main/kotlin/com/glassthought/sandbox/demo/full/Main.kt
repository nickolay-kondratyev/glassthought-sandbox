package com.glassthought.sandbox.demo.full

import com.glassthought.sandbox.demo.benchmark.ParsingBenchmarker

suspend fun main(args: Array<String>) {
  ParsingBenchmarker().go()
}
