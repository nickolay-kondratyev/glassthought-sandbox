package com.glassthought.sandbox

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

// Shared counter variable
private var counter = 0

private val lock = Any()

// Function that increments the counter inside a synchronized block
private fun incrementCounter(times: Int) {
  repeat(times) {
    synchronized(lock) {
      counter++
    }
  }
}

// Main function to launch the test
fun main() = runBlocking {
  val coroutineCount = 5
  val incrementsPerCoroutine = 100_000 // Total increments = coroutineCount * incrementsPerCoroutine

  (1..coroutineCount).map {
    thread {
      incrementCounter(incrementsPerCoroutine)
    }
  }.forEach { it.join() }


  val expectedCounter = coroutineCount * incrementsPerCoroutine
  println("Expected Counter: $expectedCounter")
  println("Actual Counter: $counter")
  println("Difference between expected and actual: ${expectedCounter - counter}")
}
