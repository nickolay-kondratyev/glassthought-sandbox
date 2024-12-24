package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay

class CountdownLatch(initialCount: Int) {
  // Validate input
  init {
    require(initialCount >= 0) {
      "Count must be non-negative, but was $initialCount"
    }
  }

  // Atomic integer to track remaining count
  private val remaining = atomic(initialCount)

  // CompletableDeferred that completes when count hits zero
  private val latchCompletion = CompletableDeferred<Unit>()

  /**
   * Decrements the internal counter. If counter reaches zero, this completes the [latchCompletion].
   *
   * Calls beyond the point where count is already zero have no effect.
   */
  fun countDown() {
    /**
     * Atomically decrements the remaining counter by 1, but never below zero.
     *
     * Under the hood, `updateAndGet` uses a compare-and-set (CAS) loop:
     * 1. It reads the current value of [remaining].
     * 2. It applies the `if (current > 0) current - 1 else current` update function.
     * 3. It attempts to set the [remaining] value to this new result via CAS.
     *    - If another thread/coroutine updated [remaining] in between steps (1) and (3),
     *      the CAS fails, and `updateAndGet` automatically retries with the fresh value.
     *
     * By retrying until a single successful CAS occurs, collisions among multiple
     * parallel threads or coroutines are safely resolved. Each call will correctly
     * decrement the counter exactly once if it’s above zero, and do nothing if it’s
     * already zero—preventing the counter from ever going negative or missing
     * decrements due to concurrency.
     */
    val updatedCount = remaining.updateAndGet { current ->
      if (current > 0) current - 1 else current
    }

    // If we just hit zero, fulfill the latch
    if (updatedCount == 0) {
      latchCompletion.complete(Unit)
    }
  }

  /**
   * Suspends until the latch has counted down to zero.
   *
   * If the latch is already at zero, this returns immediately.
   */
  suspend fun await() {
    latchCompletion.await()
  }

  fun remaining(): Int {
    return remaining.value
  }
}

fun main() = runBlocking {
  val out = Out.standard() // Assuming Out.standard() is implemented elsewhere

  val iterations = 10000000

  val decrementThreadCount = 5
  val countdownLatch = CountdownLatch((iterations * decrementThreadCount) + 1)

  val threads = (1..decrementThreadCount).map {
    thread(name = "background-thread-$it") {
      runBlocking {
        out.info("Going to count down latch $iterations times")

        repeat(iterations) {
          countdownLatch.countDown()
        }
      }
    }
  }

  val thread3Waiter= thread(name = "background-waiter") {
    runBlocking {
      out.info("Starting to wait on the latch")
      countdownLatch.await()
      out.info("Done waiting: Latch has been counted down to zero")
    }
  }

  threads.forEach { it.join() }

  if (countdownLatch.remaining() == 1) {
    out.infoGreen("CountdownLatch is equal to expected value")
  } else {
    out.infoRed("CountdownLatch is not equal to expected value")
  }

  out.info("Now going to wait 10ms prior to counting down the latch to zero")
  delay(10)
  countdownLatch.countDown()
}
