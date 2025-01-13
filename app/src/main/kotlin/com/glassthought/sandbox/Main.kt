package com.glassthought.sandbox

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import gt.sandbox.util.output.out

fun main() = runBlocking {
  val mutex = Mutex()
  val expectedOrder = listOf("Job 1", "Job 2", "Job 3")
  val results = mutableListOf<String>()

  var counterTimesRan = 0
  while (true) {
    results.clear()

   val mutexAcquired = CompletableDeferred<Unit>()
    val job1 = launch {
      out.info("Job 1: Attempting to acquire the mutex...")
      mutex.withLock {
        out.infoGreen("Job 1: Acquired the mutex!")
        mutexAcquired.complete(Unit)
        results.add("Job 1")
        delay(200) // Simulate long work
        out.infoGreen("Job 1: Releasing the mutex.")
      }
    }
    mutexAcquired.await()

    val job2AboutToWaitOnMutex = CompletableDeferred<Unit>()
    val job2 = launch {
      out.info("Job 2: Attempting to acquire the mutex...")
      job2AboutToWaitOnMutex.complete(Unit)
      mutex.withLock {
        out.infoBlue("Job 2: Acquired the mutex!")
        results.add("Job 2")
        out.infoBlue("Job 2: Releasing the mutex.")
      }
    }
    job2AboutToWaitOnMutex.await()
    delay(10)

    val job3 = launch {
      out.info("Job 3: Attempting to acquire the mutex...")
      mutex.withLock {
        out.infoRed("Job 3: Acquired the mutex!")
        results.add("Job 3")
        out.infoRed("Job 3: Releasing the mutex.")
      }
    }

    joinAll(job1, job2, job3)

    if (results != expectedOrder) {
      out.infoRed("Incorrect order detected: $results")
      break
    } else {
      out.infoGreen("Correct order: $results, (run #${++counterTimesRan})")
    }
  }
}
