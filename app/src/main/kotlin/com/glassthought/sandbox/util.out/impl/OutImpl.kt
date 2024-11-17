package gt.sandbox.util.output.impl

import gt.sandbox.util.output.Out
import kotlinx.coroutines.CoroutineName
import java.time.Instant
import kotlin.coroutines.coroutineContext

class OutSettings {
    val printThreadInfo: Boolean = true
    val printCoroutineInfo: Boolean = true
    val printTimestamp: Boolean = true
    val printElapsedTime: Boolean = true
}

class OutImpl : Out {
    val outSettings = OutSettings()
    var outInstantiationTime: Long = System.currentTimeMillis()

    override suspend fun print(msg: String) {
        kotlin.io.print(formatMsg(msg))
    }

    override suspend fun println(msg: String) {
        kotlin.io.println(formatMsg(msg))
    }

    override suspend fun printInGreen(msg: String) {
        kotlin.io.print("\u001B[32m${formatMsg(msg)}\u001B[0m")
    }

    override suspend fun printInRed(msg: String) {
        kotlin.io.print("\u001B[31m${formatMsg(msg)}\u001B[0m")
    }

    override suspend fun printInBlue(msg: String) {
        kotlin.io.print("\u001B[34m$msg\u001B[0m")
    }

    override suspend fun printlnBlue(msg: String) {
        kotlin.io.println("\u001B[34m${formatMsg(msg)}\u001B[0m")
    }

    override suspend fun printlnGreen(msg: String) {
        kotlin.io.println("\u001B[32m${formatMsg(msg)}\u001B[0m")
    }

    override suspend fun printlnRed(msg: String) {
        kotlin.io.println("\u001B[31m${formatMsg(msg)}\u001B[0m")
    }

    private suspend fun formatMsg(msg: String): String {
        val timestamp = if (outSettings.printTimestamp) "[${Instant.now()}]" else ""
        val threadInfo = if (outSettings.printThreadInfo) {
            val currentThread = Thread.currentThread()

            "[tname:${currentThread.name}/tid:${currentThread.threadId()}]"
        } else ""

        val coroutineInfo = if (outSettings.printCoroutineInfo) getCurrentCoroutineName() else ""


        val elapsedMillisSinceStart =
            if (outSettings.printElapsedTime)
                "[elapsed-since-start:" +
                        String.format("%5dms", System.currentTimeMillis() - outInstantiationTime) +
                        "]"
            else
                ""

        return "${timestamp}${elapsedMillisSinceStart}${threadInfo}${coroutineInfo} $msg"
    }


    private suspend fun getCurrentCoroutineName(): String {
        // Access the current coroutine's name if available
        val currentCoroutineName = coroutineContext[CoroutineName]?.name
        return if (currentCoroutineName != null) {
            "[coroutine:$currentCoroutineName]"
        } else {
            "[coroutine:unnamed]"
        }
    }
}
