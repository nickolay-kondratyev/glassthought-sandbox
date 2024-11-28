package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

suspend fun main() {
  out.info("Before-1")

  val valueFromCoRoutine = suspendCoroutine { continuation ->
    out.infoNonSuspend("Before-2a")

    thread{
      Thread.sleep(500)

      out.infoNonSuspend("resuming...")
      continuation.resume("resumed-value")

    }
  }

  out.info("After called. Value from co-routine: $valueFromCoRoutine")
}
