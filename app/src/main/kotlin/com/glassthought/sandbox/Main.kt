package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val out = Out.standard()

fun main() = runBlocking {
  val job = launch {
    out.info("hi from job")
  }

  delay(100)
  out.info("IsActive: " + job.isActive.toString())
}
