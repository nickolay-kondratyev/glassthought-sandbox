package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

val out = Out.standard()

fun main() = runBlocking {
  val future = CompletableFuture.supplyAsync {
    throw RuntimeException("original-exc-msg-from-supplyAsync-future-block")
    out.println("supplyAsync")
    "Jon Snow"
  }

  future
    .exceptionally { ex: Throwable? -> fallbackMaker(ex) }
    .thenAccept { x: String -> out.println(x) }

  delay(100)
  println()
}

private fun fallbackMaker(ex: Throwable?): String {
  return "Fallback Result (${ex?.message})"
}
