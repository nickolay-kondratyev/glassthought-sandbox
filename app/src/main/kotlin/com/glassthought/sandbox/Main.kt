package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.CompletableFuture

val out = Out.standard()

fun main() = runBlocking {
  val future = CompletableFuture.supplyAsync {

    throw RuntimeException("original-exc-msg-from-supplyAsync-future-block")
    "Jon Snow"
  }

  

  try {
    future.get()
  } catch (e: Exception) {
    out.println("Caught exception: ${e.message}")
  }

  println("")
}

