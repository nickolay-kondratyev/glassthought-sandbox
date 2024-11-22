package gt.sandbox.util.output.impl

import gt.sandbox.util.output.Out
import java.time.Instant

class OutSettings {
  val printColorPerThread: Boolean = false
  val printThreadInfo: Boolean = true
  val printTimestamp: Boolean = true
  val printElapsedTime: Boolean = true
}

class OutImpl : Out {
  val outSettings = OutSettings()
  var outInstantiationTime: Long = System.currentTimeMillis()

  override fun print(msg: String) {
    kotlin.io.print(optionallyColorPerThread(formatMsg(msg)))
  }

  override fun println(msg: String) {
    kotlin.io.println(optionallyColorPerThread(formatMsg(msg)))
  }

  private val threadIdToColor = mutableMapOf<Long, String>()

  // Grab a color per thread based on the thread id,
  // If run out of colors then use plain printing.
  // Keep track of which thread ids were used to find out if
  // we have ran out of colors.
  @Synchronized
  private fun optionallyColorPerThread(msg: String): Any {
    if (!outSettings.printColorPerThread) {
      return msg
    }

    val threadId = Thread.currentThread().threadId()

    val colors = listOf(
      "\u001B[32m", // green
      "\u001B[33m", // yellow
      "\u001B[34m", // blue
      "\u001B[35m", // purple
      "\u001B[36m", // cyan
      "\u001B[37m",  // white
      // "\u001B[31m", // not using red, as it is used for errors too often.
    )

    // let's figure out the color.
    // first let's check if we already have a color for this thread.
    val isThreadIdInMap = threadIdToColor.containsKey(threadId)
    if (isThreadIdInMap) {
      return "${threadIdToColor[threadId]}$msg\u001B[0m"
    }

    // At this point we dont have a color for this thread:
    // Let's find out if we can fit a new color for this thread by checking
    // the size of recorded threads and the size of colors.
    if (threadIdToColor.size < colors.size) {
      val color = colors[threadIdToColor.size]
      threadIdToColor[threadId] = color
      return "$color$msg\u001B[0m"
    }

    // At this point we have ran out of colors, so we will just print the message
    // without any color.
    return msg
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
