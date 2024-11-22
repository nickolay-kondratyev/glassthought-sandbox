package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.CompletableFuture

val out = Out.standard()

fun main() = runBlocking {
  val future = CompletableFuture.supplyAsync {
    out.println("supplyAsync")
    throw RuntimeException("original-exc-msg-from-supplyAsync-future-block")
    "Jon Snow"
  }


  future.handle { result: String, ex: Throwable? ->
    if (ex != null) {
      return@handle "Error handling: " + ex.message
    } else {
      return@handle result.uppercase(Locale.getDefault())
    }
  }.thenApply { x: String -> out.println("thenApply"); x + "!" }
    .thenAccept { x: String -> out.println(x) }

  delay(100)
  println()
}

private fun fallbackMaker(ex: Throwable?): String {
  return "Fallback Result (${ex?.message})"
}
