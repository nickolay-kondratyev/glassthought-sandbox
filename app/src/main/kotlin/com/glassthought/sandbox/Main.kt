package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

// Do not do this
var continuation: Continuation<Unit>? = null
suspend fun suspendAndSetContinuation() {

  out.info("Suspending coroutine")
  suspendCoroutine<Unit> { cont ->
    continuation = cont
  }
}

suspend fun main() {
  out.info("Before")
  suspendAndSetContinuation()

  // We will never get here as the co-routine was suspended without
  // ever being resumed
  continuation?.resume(Unit)
  out.info ("After")
}
