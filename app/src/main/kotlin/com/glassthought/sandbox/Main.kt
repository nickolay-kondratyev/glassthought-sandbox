package com.glassthought.sandbox

import gt.sandbox.util.output.println
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val seq = sequence {
    yield(1)
    yield(2)
    yield(3)
  }

  println(seq.toList())
}
