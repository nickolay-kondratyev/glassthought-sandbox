package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

val indexParsed = CompletableDeferred<Unit>()
val directoriesWatched = CompletableDeferred<Unit>()

suspend fun waitForAllStates() {
  // Wait for both states to complete
  indexParsed.await()
  directoriesWatched.await()
}

fun markIndexParsed() {
  indexParsed.complete(Unit)
}

fun markDirectoriesWatched() {
  directoriesWatched.complete(Unit)
}

val out = Out.standard()

suspend fun main(args: Array<String>) {
  out.info("Hello, world!")
  thread {
    runBlocking {
      out.info("Sleeping")
      Thread.sleep(1000)
      markIndexParsed()
      markDirectoriesWatched()
      out.info("Done Sleeping")
    }
  }

  waitForAllStates()
  delay(3)
  out.info("Goodbye, world!")
}
