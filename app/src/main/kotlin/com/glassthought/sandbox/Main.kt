package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

private val executor = Executors.newSingleThreadScheduledExecutor {
  Thread(it, "my-scheduler").apply { isDaemon = true }
}


suspend fun main() {
  out.info("Before-1")

  suspendCoroutine<Unit> { continuation ->
    // This lambda function is called right before the suspension
    // takes place.
    out.infoNonSuspend(continuation.toString())
    out.infoNonSuspend("Before-2a")

    executor.schedule({
      out.infoNonSuspend("Resuming")

      continuation.resume(Unit)
    }, 1000, TimeUnit.MILLISECONDS)
  }

  out.info("After called because we resumed")
}
