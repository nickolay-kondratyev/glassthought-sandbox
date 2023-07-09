package gt.kotlin.sandbox.main.threading

import gt.kotlin.sandbox.internal.output.Out
import gt.kotlin.sandbox.internal.output.impl.OutImpl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

interface Server {
    fun start()
    fun stop()
}

class ServerImpl(
    private val out: Out,
) : Server, CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    override fun start() {
        out.printlnRed("Starting server")

        launch {
            out.printlnBlue("Running server work in thread: ${Thread.currentThread().name}")
            delay(2000)
            out.printlnBlue("Server work completed")
        }

        launch {
            out.printlnGreen("Running additional server work in thread: ${Thread.currentThread().name}")
            delay(1500)
            out.printlnGreen("Additional server work completed")
        }
    }

    suspend fun joinOnJob() {
        job.join()
    }

    override fun stop() {
        out.printlnRed("Stopping server")
        job.cancel()
    }
}


fun main() = runBlocking {
    go()
}


private suspend fun go() {
    val out = OutImpl()

    println("--------------------------------------------------------------------------------")
    out.println("Example where there server is aborted prior to finishing:")
    val server = ServerImpl(out)
    server.start()

    println("Waiting on job to finish")
    server.joinOnJob()

    out.printlnRed("Server done processing")
}

