package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun main() = runBlocking {
  val out = Out.standard() // Assuming Out.standard() is implemented elsewhere

  // Shared variable
  var counter = 0
  val iterations = 10000000

  // Threads
  val thread1 = thread {
    repeat(iterations) {
      counter++
    }
  }

  val thread2 = thread {
    repeat(iterations) {
      counter++
    }
  }

  // Wait for threads to finish
  thread1.join()
  thread2.join()

  // Display results
  val expected = iterations * 2
  out.infoGreen("Expected value: $expected actual value: $counter.")
  if (counter != expected) {
    out.infoRed("Counter is not equal to expected value, difference: ${expected - counter}")
  } else {
    out.infoGreen("Counter is equal to expected value")
  }
}
