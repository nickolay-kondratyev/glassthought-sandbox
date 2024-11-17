package com.glassthought.sandbox

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val seedValue = 1
  
  generateSequence(seedValue) { it + 1 }
    .take(10)
    .forEach { print("$it, ") }
}
