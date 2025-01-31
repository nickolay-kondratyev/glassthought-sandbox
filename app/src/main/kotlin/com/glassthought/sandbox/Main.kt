package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*

interface Server {
  suspend fun start()
  suspend fun stop()
}

val out = Out.standard()

class ServerImpl(
  private val scope: CoroutineScope // Injected scope for better control
) : Server {

  private val job = SupervisorJob() // Ensures independent coroutines
  private val serverScope = scope + job

  override suspend fun start() {
    out.info("Starting server")

    serverScope.launch(CoroutineName("ServerWork-1")) {
      out.info("Running server work in thread: ${Thread.currentThread().name}")
      delay(2000)
      out.info("ServerWork-1 completed")
    }

    serverScope.launch(CoroutineName("ServerWork-2")) {
      out.info("Running additional server work in thread: ${Thread.currentThread().name}")
      delay(1500)
      out.info("ServerWork-2  completed")
    }
  }

  override suspend fun stop() {
    out.info("Stopping server")

    job.cancel()
  }
}

fun main() = runBlocking {
  out.info("--------------------------------------------------------------------------------")
  out.info("Example where the server is aborted prior to finishing:")
  runWithDelayBeforeStopping(1000)

  out.info("--------------------------------------------------------------------------------")
  out.info("Example where the server has time to finish:")
  runWithDelayBeforeStopping(3000)
}

private suspend fun runWithDelayBeforeStopping(delayBeforeStopping: Long) {
  val server = ServerImpl(CoroutineScope(Dispatchers.Default)) // Inject external scope
  server.start()
  delay(delayBeforeStopping) // Use delay instead of Thread.sleep
  server.stop()
}
