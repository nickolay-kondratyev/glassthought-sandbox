package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import java.util.concurrent.CompletableFuture

val out = Out.standard()


fun main() {
  // Create a CompletableFuture that simulates a long-running task
  val future = CompletableFuture.supplyAsync {
    out.println("Task started: ${Thread.currentThread().name}")
    try {
      Thread.sleep(5000) // Simulate a long-running task
      "Task completed"
    } catch (e: InterruptedException) {
      out.println("Task was interrupted")
      throw RuntimeException("Task interrupted", e)
    }
  }

  // Simulate cancellation after 2 seconds
  Thread {
    Thread.sleep(2000) // Wait 2 seconds before canceling
    out.println("Cancelling the future...")
    val cancelled = future.cancel(true)
    out.println("Future cancelled: $cancelled")
  }.start()

  // Attempt to retrieve the result (blocks until completed or cancelled)
  try {
    val result = future.get()
    out.println("Future result: $result")
  } catch (e: Exception) {
    out.println("Exception occurred: ${e.message}")
  }

  out.println("Main thread ends")
}
