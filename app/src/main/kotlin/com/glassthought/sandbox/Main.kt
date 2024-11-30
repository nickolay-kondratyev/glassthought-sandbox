package com.glassthought.sandbox

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

// Shared counter variable
private var counter = 0

// Function that increments the counter inside a synchronized block
private suspend fun incrementCounter(times: Int) {
  repeat(times) {
    synchronized(counter) {
      counter++
    }
  }
}

// Main function to launch the test
fun main() = runBlocking {
  val coroutineCount = 5
  val incrementsPerCoroutine = 100_000 // Total increments = coroutineCount * incrementsPerCoroutine

  val timeTaken = measureTimeMillis {
    coroutineScope {
      repeat(coroutineCount) {
        launch(Dispatchers.Default) { incrementCounter(incrementsPerCoroutine) }
      }
    }
  }

  val expectedCounter = coroutineCount * incrementsPerCoroutine
  println("Expected Counter: $expectedCounter")
  println("Actual Counter: $counter")
  println("Difference between expected and actual: ${expectedCounter - counter}")
  println("Time Taken: $timeTaken ms")
}
