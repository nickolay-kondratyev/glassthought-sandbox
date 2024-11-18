package com.glassthought.sandbox

import kotlinx.coroutines.runBlocking

val seq = sequence {
  println("Generating first")
  yield(1)
  println("Generating second")
  yield(2)
  println("Generating third")
  yield(3)
  println("Done")
}

fun main() = runBlocking {
  println(seq.take(1).toList())
}
