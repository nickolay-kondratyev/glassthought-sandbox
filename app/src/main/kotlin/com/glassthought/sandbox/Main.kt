package com.glassthought.sandbox

import kotlin.system.measureNanoTime

fun main() {
  val iterations = 100_000_000

  // Operation without synchronization
  var counterWithoutSync = 0
  val timeWithoutSync = measureNanoTime {
    repeat(iterations) {
      counterWithoutSync++
    }
  }

  println("Counter without synchronization: $counterWithoutSync")
  println("Time taken without synchronization: $timeWithoutSync ns")

  // Operation with synchronized block
  val lock = Any()
  var counterWithSync = 0
  val timeWithSync = measureNanoTime {
    repeat(iterations) {
      synchronized(lock) {
        counterWithSync++
      }
    }
  }

  println("Counter with synchronized block: $counterWithSync")
  println("Time taken with synchronized block: $timeWithSync ns")

  // Calculate overhead per synchronization operation
  val overheadPerOperation = (timeWithSync - timeWithoutSync) / iterations
  println("Overhead of synchronized{} per operation: $overheadPerOperation ns")

  // Calculate how many times synchronization can occur in one millisecond
  val timesPerMillisecond = 1_000_000 / overheadPerOperation
  println("How many times can synchronized block be executed in one millisecond: $timesPerMillisecond")
}
