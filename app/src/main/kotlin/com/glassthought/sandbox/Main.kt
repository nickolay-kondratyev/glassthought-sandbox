package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

// Do not do this, potential memory leak
var continuation: Continuation<Unit>? = null
suspend fun suspendAndSetContinuation() {
  suspendCoroutine<Unit> { cont ->
    continuation = cont
  }
}

suspend fun main() = coroutineScope {
  out.info("Before")

  launch(CoroutineName("launch-1")) {
    out.info("In 'launch' before delay")
    delay(1000)
    out.info("In 'launch' after delay: resuming continuation")

    continuation?.resume(Unit)
  }
  suspendAndSetContinuation()
  out.info("After")
}
