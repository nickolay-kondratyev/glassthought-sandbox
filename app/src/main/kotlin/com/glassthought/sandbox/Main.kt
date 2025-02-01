package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*

interface Server {
  suspend fun start()
  suspend fun stop()
  suspend fun join()
}

val out = Out.standard()

class ServerImpl(
  private val serverName: String,
  private val scope: CoroutineScope // Injected scope for better control
) : Server {
  private var backgroundJob: Job? = null

  override suspend fun start() {
    out.info("Starting server")

    backgroundJob = scope.launch(CoroutineName("${serverName}-work-1")) {
      out.info("Running server work in thread: ${Thread.currentThread().name}")
      delay(2000)
      out.info("${serverName}-work-1 completed")
    }
  }

  override suspend fun stop() {
    out.info("Stopping server: $serverName")

    scope.cancel()
  }

  override suspend fun join() {
    if (backgroundJob != null) {
      backgroundJob!!.join()
    }
  }
}

fun main() = runBlocking {
  val server1 = ServerImpl("server-1", CoroutineScope(Dispatchers.Default))
  val server2 = ServerImpl("server-2", CoroutineScope(Dispatchers.Default))

  server1.start()
  server2.start()

  server1.stop()

  server1.join()
  server2.join()
}
