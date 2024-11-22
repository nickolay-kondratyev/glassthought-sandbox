package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val out = Out.standard()

suspend fun main(): Unit = coroutineScope {
  val job = launch (CoroutineName("job")){
    repeat(1_000) { i ->
      delay(200)
      out.info("Printing $i")
    }
  }

  delay(1100)
  job.cancel()
  job.join()

  out.info("Cancelled successfully")
}
