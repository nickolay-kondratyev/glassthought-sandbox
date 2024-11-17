package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


val out = Out.standard()

class AttemptToGrabLockFromSameCoroutine {
  private var value = 0
  private val mutex = Mutex() // Mutex for coroutine-safe access to 'value'

  // Suspend function to use coroutine-friendly synchronization with timeout
  suspend fun outerCall() {
    out.println("outerCall outside of mutex")

    // Attempt to acquire the lock with a timeout
    val success = withTimeoutOrNull(1000L) { // 1000ms timeout
      mutex.withLock {
        out.println("outerCall INSIDE of mutex")
        innerCall()
      }
    }

    if (success == null) {
      out.println("ERROR: outerCall could not acquire the lock within timeout")
    }
  }

  private suspend fun innerCall() {
    out.println("innerCall outside of mutex")

    // Attempt to acquire the lock with a timeout
    val success = withTimeoutOrNull(1000L) { // 1000ms timeout
      mutex.withLock {
        out.println("innerCall INSIDE of mutex")
        value++
      }
    }

    if (success == null) {
      out.println("ERROR: innerCall could not acquire the lock within timeout")
    }
  }

  fun getValue() = value
}

fun main() = runBlocking {
  val attemptToGrabLockFromSameCoroutine = AttemptToGrabLockFromSameCoroutine()

  // Launch coroutines instead of creating threads
  coroutineScope {
    val jobs = (1..2).map {
      launch { attemptToGrabLockFromSameCoroutine.outerCall() }
    }
    jobs.forEach { it.join() } // Wait for all coroutines to complete
  }

  println("Value: ${attemptToGrabLockFromSameCoroutine.getValue()}")
}
