package com.glassthought.sandbox

// Required dependencies:
// implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
// implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.system.measureNanoTime
import java.io.File

/**
 * Data class representing an object with 4 string fields.
 */
@Serializable
data class TestObject(
  val field1: String,
  val field2: String,
  val field3: String,
  val field4: String
)

/**
 * Benchmarks the serialization of [TestObject] and appends the serialized JSON to a file.
 *
 * @param iterations The number of times to perform the serialization and file write.
 * @return The total time in nanoseconds taken for all iterations.
 */
fun benchmarkSerializationAndFileWrite(iterations: Int): Long {
  // JSON serializer with default configuration.
  val json = Json { encodeDefaults = true }
  // Use a fixed file in /tmp. Ensure it exists.
  val filePath = "/tmp/serialization_benchmark.txt"
  File(filePath).apply {
    if (!exists()) createNewFile()
  }

  var totalTimeNs = 0L
  repeat(iterations) {
    // For consistency, the object is always the same.
    val testObj = TestObject("value1", "value2", "value3", "value4")
    // Measure the time taken for serializing and appending to file.
    val timeNs = measureNanoTime {
      // Serialize the object to JSON.
      val serialized = json.encodeToString(TestObject.serializer(), testObj)
      // Append the serialized string to the file (with newline).
      File(filePath).appendText("$serialized\n")
    }
    totalTimeNs += timeNs
  }
  return totalTimeNs
}

/**
 * Benchmarks sending [TestObject] instances to an infinitely buffered channel.
 *
 * @param iterations The number of times to send the object.
 * @param channel The [Channel] used for sending objects.
 * @return The total time in nanoseconds taken for all send operations.
 */
suspend fun benchmarkChannelSend(iterations: Int, channel: Channel<TestObject>): Long {
  var totalTimeNs = 0L
  repeat(iterations) {
    val testObj = TestObject("value1", "value2", "value3", "value4")
    // Measure the time taken to send the object into the channel.
    val timeNs = measureNanoTime {
      channel.send(testObj)
    }
    totalTimeNs += timeNs
  }
  return totalTimeNs
}

/**
 * Entry point of the benchmarking program.
 *
 * Runs both benchmarks sequentially and prints the total and average time for each.
 */
fun main() = runBlocking {
  // Total number of iterations for each benchmark.
  val iterations = 1000
  println("Starting benchmarks with $iterations iterations.\n")

  // Benchmark serialization and file write.
  val totalTimeSerialization = benchmarkSerializationAndFileWrite(iterations)
  println("Serialization & File Append Benchmark:")
  println("  Total time: $totalTimeSerialization ns / ${totalTimeSerialization / 1_000_000} ms")

  println("  Average per operation: ${totalTimeSerialization / iterations} ns\n")

  // Create an infinitely buffered channel.
  val channel = Channel<TestObject>(Channel.UNLIMITED)
  // Launch a simple consumer to drain the channel and avoid blocking.
  val consumerJob = launch {
    for (obj in channel) {
      // Minimal processing can be added here if needed.
    }
  }
  // Benchmark sending objects to the channel.
  val totalTimeChannelSend = benchmarkChannelSend(iterations, channel)
  println("Channel Send Benchmark:")
  println("  Total time: $totalTimeChannelSend ns / ${totalTimeChannelSend / 1_000_000} ms")
  println("  Average per operation: ${totalTimeChannelSend / iterations} ns\n")

  // Cleanup: close the channel and cancel the consumer.
  channel.close()
  consumerJob.cancelAndJoin()

  println("Benchmarks completed.")
}
