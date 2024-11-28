package com.glassthought.sandbox

import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun randomNumbersUpTo1000(
  seed: Long = System.currentTimeMillis()
): Sequence<Int> = sequence {
  val random = Random(seed)

  while (true) {
    yield(random.nextInt(0, 1000))
  }
}

fun main() = runBlocking {
  println(randomNumbersUpTo1000().take(3).toList())
}
