package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val out = Out.standard()

suspend fun main(): Unit = coroutineScope {
  val job = launch {
    repeat(1_000) { i ->
      delay(200)
      out.println("Printing $i")
    }
  }

  delay(1100)
  job.cancel()
  job.join()

  out.println("Cancelled successfully")
}
