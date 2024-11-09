package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

val out = Out.standard();

class RaceConditionInducer {
  private var value = 0
  private val mutex = Mutex() // Mutex for coroutine-safe access to 'value'

  // Suspend function to use coroutine-friendly synchronization
  suspend fun incrementValue() {
    mutex.withLock {
      val storedValue = value

      // Coroutine-friendly delay, instead of Thread.sleep
      // [delay](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/delay.html)
      delay(100)
      out.println("Incrementing value from $storedValue to ${storedValue + 1}")

      value = storedValue + 1
    }
  }

  fun getValue() = value
}

fun main() = runBlocking {
  val raceConditionInducer = RaceConditionInducer()

  val dispatchers = listOf(
    Dispatchers.Default,
    Dispatchers.IO,
    newFixedThreadPoolContext(3, "CustomThreadPool")
  )

  val jobs = dispatchers.mapIndexed { index, dispatcher ->
    launch(dispatcher) {
      out.println("Starting scope $index on dispatcher $dispatcher")
      repeat(5) { i ->
        raceConditionInducer.incrementValue()
      }
      println("Finished scope $index on dispatcher $dispatcher")
    }
  }

  jobs.forEach { it.join() }

  println("Final Value: ${raceConditionInducer.getValue()}")
}
