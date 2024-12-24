package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun main() = runBlocking {
  val out = Out.standard() // Assuming Out.standard() is implemented elsewhere

  // Shared variable
  val counter = AtomicInteger(0)
  val iterations = 10000000

  // Threads
  val thread1 = thread {
    repeat(iterations) {
      counter.incrementAndGet()
    }
  }

  val thread2 = thread {
    repeat(iterations) {
      counter.incrementAndGet()
    }
  }

  // Wait for threads to finish
  thread1.join()
  thread2.join()

  // Display results
  val expected = iterations * 2
  val finalValue = counter.get()
  out.infoGreen("Expected value: $expected actual value: $finalValue.")
  if (finalValue != expected) {
    out.infoRed("Counter is not equal to expected value, difference: ${expected - finalValue}")
  } else {
    out.infoGreen("Counter is equal to expected value")
  }
}
