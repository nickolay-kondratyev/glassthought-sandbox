package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

suspend fun main() {
  out.info("Before-1")

  suspendCoroutine<String>{ continuation ->
    // This lambda function is called right before the suspension
    // takes place.
    out.infoNonSuspend(continuation.toString())

    out.infoNonSuspend("Before-2")

    // Question: where can we catch the result that we resumed with?
    continuation.resume("Hello from suspended coroutine")
  }

  out.info("After called because we resumed")
}
