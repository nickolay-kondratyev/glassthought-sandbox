package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*

val indexParsed = CompletableDeferred<Unit>()
val directoriesWatched = CompletableDeferred<Unit>()

val out = Out.standard()

suspend fun waitForAllStates() {
  out.info("Starting to wait for all states")

  // Wait for both states to complete
  indexParsed.await()
  directoriesWatched.await()

  out.info("Finished waiting")
}

fun markIndexParsed() {
  indexParsed.complete(Unit)
}

fun markDirectoriesWatched() {
  directoriesWatched.complete(Unit)
}


suspend fun main(args: Array<String>) {
  out.info("Starting the application")

  val scope = CoroutineScope(Dispatchers.Default)

  val job1 = scope.launch(CoroutineName("UpdateNews")) {

    out.info("Sleeping")
    Thread.sleep(1000)
    markIndexParsed()
    markDirectoriesWatched()
    out.info("Marked deferred features as done")
  }

  val job2 = scope.launch(CoroutineName("WaitForAllStates")) {
    waitForAllStates()
    out.info("Done waiting.")
  }

  job1.join()
  job2.join()
}

