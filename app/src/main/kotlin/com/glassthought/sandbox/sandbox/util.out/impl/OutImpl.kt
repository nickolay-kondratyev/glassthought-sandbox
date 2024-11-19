package gt.sandbox.util.output.impl

import gt.sandbox.util.output.Out
import java.time.Instant

class OutSettings {
    val printThreadInfo: Boolean = true
    val printTimestamp: Boolean = true
    val printElapsedTime: Boolean = true
}

class OutImpl : Out {
    val outSettings = OutSettings()
    var outInstantiationTime: Long = System.currentTimeMillis()

    override fun print(msg: String) {
        kotlin.io.print(formatMsg(msg))
    }

    override fun println(msg: String) {
        kotlin.io.println(formatMsg(msg))
    }

    override fun printInGreen(msg: String) {
        kotlin.io.print("\u001B[32m${formatMsg(msg)}\u001B[0m")
    }

    override fun printInRed(msg: String) {
        kotlin.io.print("\u001B[31m${formatMsg(msg)}\u001B[0m")
    }

    override fun printInBlue(msg: String) {
        kotlin.io.print("\u001B[34m$msg\u001B[0m")
    }

    override fun printlnBlue(msg: String) {
        kotlin.io.println("\u001B[34m${formatMsg(msg)}\u001B[0m")
    }

    override fun printlnGreen(msg: String) {
        kotlin.io.println("\u001B[32m${formatMsg(msg)}\u001B[0m")
    }

    override fun printlnRed(msg: String) {
        kotlin.io.println("\u001B[31m${formatMsg(msg)}\u001B[0m")
    }

    private fun formatMsg(msg: String): String {
        val timestamp = if (outSettings.printTimestamp) "[${Instant.now()}]" else ""
        val threadInfo = if (outSettings.printThreadInfo) {
            val currentThread = Thread.currentThread()


            "[tname:${currentThread.name}/tid:${currentThread.threadId()}]"
        } else ""

        val elapsedMillisSinceStart =
            if (outSettings.printElapsedTime)
                "[elapsed-since-start:" +
                        String.format("%5dms", System.currentTimeMillis() - outInstantiationTime) +
                        "]"
            else
                ""

        return "$timestamp$elapsedMillisSinceStart$threadInfo $msg"
    }
}
