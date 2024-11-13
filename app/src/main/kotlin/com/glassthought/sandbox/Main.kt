package com.glassthought.sandbox

import kotlinx.coroutines.*
import kotlin.random.Random

class RaceConditionInducer {
  private var value = 0

  // Suspending function to simulate some work and induce a race condition
  suspend fun incrementValue() {
    val storedValue = value

    // Simulate delay to increase the likelihood of race conditions
    delay(10)

    println("Incrementing value from storedValue=$storedValue to ${storedValue + 1}")
    value = storedValue + 1
  }

  fun getValue() = value
}

fun main() = runBlocking {
  val raceConditionInducer = RaceConditionInducer()

  // Launch multiple coroutines to increment the value
  val jobs = List(10) {
    GlobalScope.launch {
      raceConditionInducer.incrementValue()
    }
  }

  // Wait for all coroutines to complete
  jobs.forEach { it.join() }

  println("Final Value: ${raceConditionInducer.getValue()}")
}
