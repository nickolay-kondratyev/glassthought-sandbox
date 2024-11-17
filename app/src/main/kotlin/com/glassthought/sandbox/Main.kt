package com.glassthought.sandbox

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  generateSequence(1) { it + 1 }
    .take(10)
    .forEach { print("$it, ") }
}
