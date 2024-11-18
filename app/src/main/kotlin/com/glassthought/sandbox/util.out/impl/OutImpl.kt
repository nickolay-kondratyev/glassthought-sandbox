package com.glassthought.sandbox.util.out.impl

import gt.sandbox.util.output.Out
import java.time.Instant

data class OutSettings(
  val printThreadInfo: Boolean = true,
  val printTimestamp: Boolean = false,
  val printElapsedTime: Boolean = true
)


class OutImpl(private val outSettings: OutSettings) : Out {
  var outInstantiationTime: Long = System.currentTimeMillis()

  override fun print(msg: String) {
    kotlin.io.print(msg)
  }

  override fun println(msg: String) {
    kotlin.io.println(msg)
  }

  override fun printlnGreen(msg: String) {
    kotlin.io.println("\u001B[32m${msg}\u001B[0m")
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

  override fun info(msg: String) {
    kotlin.io.println(formatMsg(msg))
  }

  override fun infoBlue(msg: String) {
    kotlin.io.println("\u001B[34m${msg}\u001B[0m")
  }

  override fun infoGreen(msg: String) {
    kotlin.io.println("\u001B[32m${formatMsg(msg)}\u001B[0m")
  }

  override fun infoRed(msg: String) {
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
        "[elapsed:" +
            String.format("%5dms", System.currentTimeMillis() - outInstantiationTime) +
            "]"
      else
        ""

    return "${timestamp}${elapsedMillisSinceStart}${threadInfo} $msg"
  }
}
