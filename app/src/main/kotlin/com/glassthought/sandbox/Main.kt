package com.glassthought.sandbox

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RaceConditionInducer {
  private var value = 0
  private val mutex = Mutex() // Mutex for coroutine-safe access to 'value'

  // Suspend function to use coroutine-friendly synchronization
  suspend fun incrementValue() {
    mutex.withLock {
      val storedValue = value

      // Coroutine-friendly delay, instead of Thread.sleep
      // [delay](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/delay.html)
      delay(10)

      println("Incrementing value from storedValue=$storedValue to ${storedValue + 1}")
      value = storedValue + 1
    }
  }

  fun getValue() = value
}

fun main() = runBlocking {
  val raceConditionInducer = RaceConditionInducer()

  // Launch coroutines instead of creating threads
  coroutineScope {
    val jobs = (1..10).map {
      launch { raceConditionInducer.incrementValue() }
    }
    jobs.forEach { it.join() } // Wait for all coroutines to complete
  }

  println("Value: ${raceConditionInducer.getValue()}")
}
