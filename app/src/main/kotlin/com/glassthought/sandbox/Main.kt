package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val out = Out.standard() // Assuming this is implemented for logging

  out.info("Starting sandbox example")

  run {
    out.info("Entering the run block")

    delay(100)

    out.info("Exiting the run block")
  }

  out.info("Run block complete, continuing execution")
}
