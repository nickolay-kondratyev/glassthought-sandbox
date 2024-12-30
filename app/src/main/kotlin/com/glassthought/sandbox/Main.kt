package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

var sharedCounter = 0 // Shared resource

val out = Out.standard()

@Synchronized
fun incrementSharedCounter(){
  sharedCounter++
}

fun main() = runBlocking {
  val coroutineCount = 5
  val iterationsPerCoroutine = 1000000

  val timeTaken = measureTimeMillis {
    val jobs = List(coroutineCount) { idx ->
      launch(CoroutineName("coroutine-$idx") + Dispatchers.IO) {
        out.info("Starting coroutine $idx")

        repeat(iterationsPerCoroutine) {
          incrementSharedCounter()
        }
      }
    }

    jobs.forEach { it.join() } // Wait for all coroutines to finish
  }

  out.info("Expected counter value: ${coroutineCount * iterationsPerCoroutine}")
  out.info("Actual counter value: $sharedCounter")
  out.info("Time taken: $timeTaken ms")
}
