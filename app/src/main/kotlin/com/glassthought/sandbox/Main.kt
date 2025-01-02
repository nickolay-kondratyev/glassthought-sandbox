package com.glassthought.sandbox

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureNanoTime

fun main() = runBlocking {
  val iterations = 1_000_000

  // Operation without mutex
  var counterWithoutMutex = 0
  val timeWithoutMutex = measureNanoTime {
    repeat(iterations) {
      counterWithoutMutex++
    }
  }

  println("Counter without mutex: $counterWithoutMutex")
  println("Time taken without mutex: $timeWithoutMutex ns")

  // Operation with mutex
  val mutex = Mutex()
  var counterWithMutex = 0
  val timeWithMutex = measureNanoTime {
    repeat(iterations) {
      mutex.withLock {
        counterWithMutex++
      }
    }
  }

  println("Counter with mutex: $counterWithMutex")
  println("Time taken with mutex: $timeWithMutex ns")

  // Calculate overhead per mutex operation
  val overheadPerOperation = (timeWithMutex - timeWithoutMutex) / iterations
  println("Overhead of Mutex.withLock{} per operation: $overheadPerOperation ns")

  // In relation to millisecond how many times can we take mutex in one millisecond
  // There is 1000 microseconds in one millisecond
  // There is 1,000,000 in one millisecond
  val timesPerMillisecond = 1_000_000 / overheadPerOperation
  println("In relation to millisecond how many times can we take mutex in one millisecond: $timesPerMillisecond")
}
