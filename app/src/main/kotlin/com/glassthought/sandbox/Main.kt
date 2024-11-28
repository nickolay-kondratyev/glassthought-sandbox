package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

suspend fun main() {
  out.info("Before-1")

  suspendCoroutine<Unit> { continuation ->
    out.infoNonSuspend(continuation.toString())

    out.infoNonSuspend("Before-2")
  }

  out.info("After never called")
}
