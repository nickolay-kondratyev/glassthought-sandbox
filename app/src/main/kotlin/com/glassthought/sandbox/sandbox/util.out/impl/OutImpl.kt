package com.glassthought.sandbox.sandbox.util.out.impl

import gt.sandbox.util.output.Out
import kotlinx.coroutines.CoroutineName
import java.time.Instant
import kotlin.coroutines.coroutineContext

data class OutSettings(
  val printThreadInfo: Boolean = true,
  val printCoroutineName: Boolean = true,
  val printTimestamp: Boolean = false,
  val printElapsedTime: Boolean = true,
  val printColorPerCoroutine: Boolean = true
)

/** Emoji: 🥇 (used to denote the main thread) */
private const val EMOJI_MEDAL_NUMBER_1 = "\uD83E\uDD47"

class OutImpl(private val outSettings: OutSettings) : Out {
  private var outInstantiationTime: Long = System.currentTimeMillis()
  private val coroutineNameToColor = mutableMapOf<String, String>()

  private val colors = listOf(
    "\u001B[32m", // green
    "\u001B[33m", // yellow
    "\u001B[34m", // blue
    "\u001B[35m", // purple
    "\u001B[36m", // cyan
  )

  override fun print(msg: String) {
    kotlin.io.print(msg)
  }

  override fun println(msg: String) {
    kotlin.io.println(msg)
  }

  override fun printGreen(msg: String) {
    kotlin.io.print("\u001B[32m${msg}\u001B[0m")
  }

  override fun printRed(msg: String) {
    kotlin.io.print("\u001B[31m${msg}\u001B[0m")
  }

  override fun printBlue(msg: String) {
    kotlin.io.print("\u001B[34m${msg}\u001B[0m")
  }


  override suspend fun info(msg: String) {
    kotlin.io.println(optionallyColorPerCoroutine(formatMsg(msg)))
  }

  override fun infoNonSuspend(msg: String) {
    kotlin.io.println(formatMsgNonSuspend(msg))
  }

  override suspend fun infoBlue(msg: String) {
    kotlin.io.println("\u001B[34m${formatMsg(msg)}\u001B[0m")
  }

  override suspend fun infoGreen(msg: String) {
    kotlin.io.println("\u001B[32m${formatMsg(msg)}\u001B[0m")
  }

  override suspend fun infoRed(msg: String) {
    kotlin.io.println("\u001B[31m${formatMsg(msg)}\u001B[0m")
  }

  private suspend fun optionallyColorPerCoroutine(msg: String): String {
    if (!outSettings.printColorPerCoroutine) {
      return msg
    }

    val coroutineName = coroutineContext[CoroutineName]?.name ?: return msg

    synchronized(coroutineNameToColor) {
      if (!coroutineNameToColor.containsKey(coroutineName)) {
        if (coroutineNameToColor.size < colors.size) {
          coroutineNameToColor[coroutineName] = colors[coroutineNameToColor.size]
        } else {
          return msg // No color available, print in plain text
        }
      }
    }

    val color = coroutineNameToColor[coroutineName]
    return "$color$msg\u001B[0m"
  }

  private suspend fun formatMsg(msg: String): String {
    val timestamp = if (outSettings.printTimestamp) "[${Instant.now()}]" else ""
    val threadInfo = formatThreadInfo()
    val coroutineInfo = if (outSettings.printCoroutineName) getCurrentCoroutineName() else ""

    val elapsedMillisSinceStart =
      if (outSettings.printElapsedTime)
        "[elapsed:" +
            String.format("%5dms", System.currentTimeMillis() - outInstantiationTime) +
            "]"
      else
        ""

    return "${timestamp}${elapsedMillisSinceStart}${threadInfo}${coroutineInfo} $msg"
  }

  private fun formatMsgNonSuspend(msg: String): String {
    val timestamp = if (outSettings.printTimestamp) "[${Instant.now()}]" else ""
    val threadInfo = formatThreadInfo()

    val elapsedMillisSinceStart =
      if (outSettings.printElapsedTime)
        "[elapsed:" +
            String.format("%5dms", System.currentTimeMillis() - outInstantiationTime) +
            "]"
      else
        ""

    return "${timestamp}${elapsedMillisSinceStart}${threadInfo} $msg"
  }

  private fun formatThreadInfo() = if (outSettings.printThreadInfo) {
    val currentThread = Thread.currentThread()
    val threadId = currentThread.threadId()
    "[${getThreadIdNumberEmoji(currentThread)}/tname:${currentThread.name}/tid:$threadId]"
  } else ""


  // Will hold mapping of thread id to corresponding thread number emoji.
  // ⓶ ⓷ ⓸ ⓹ ⓺ ⓻ ⓼ ⓽ ⓾
  private val threadIdToEmojiMap = mutableMapOf<Long, String>()
  private val availableThreadEmojis = mutableListOf(
    "⓶", "⓷", "⓸", "⓹", "⓺", "⓻", "⓼", "⓽", "⓾"
  )

  /** We will assign an emoji to each new encountered thread.
   *
   * We will reserve #1 for main thread and will not use it for threads
   * that we deem other than main.
   *
   * If we run out of numbers to assign we will not show an emoji for the thread.
   * */
  private fun getThreadIdNumberEmoji(thread: Thread): String {
    val threadId = thread.threadId()

    if (threadId == 1L && thread.name == "main") {
      // 🥇
      return EMOJI_MEDAL_NUMBER_1
    } else {
      synchronized(threadIdToEmojiMap) {
        if (threadIdToEmojiMap.containsKey(threadId)) {
          return threadIdToEmojiMap[threadId]!!
        } else {
          if (availableThreadEmojis.isNotEmpty()) {
            val emoji = availableThreadEmojis.removeAt(0)
            threadIdToEmojiMap[threadId] = emoji
            return emoji
          }

          return ""
        }
      }
    }
  }

  private suspend fun getCurrentCoroutineName(): String {
    val currentCoroutineName = coroutineContext[CoroutineName]?.name
    return if (currentCoroutineName != null) {
      "[coroutine:$currentCoroutineName]"
    } else {
      "[coroutine:unnamed]"
    }
  }
}
