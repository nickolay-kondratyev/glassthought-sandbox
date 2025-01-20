package com.glassthought.sandbox

import com.glassthought.sandbox.util.out.impl.OutSettings
import gt.sandbox.util.output.Out
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val out = Out.standard(OutSettings(printCoroutineName = true, printThreadInfo = false))

fun main(args: Array<String>) {
  runBlocking {
    val channel = Channel<Int>(Channel.UNLIMITED)

    val sender = launch(CoroutineName("${Emoji.LETTER}-sender")) {
      try {
        repeat(1) {
          delay(500)

          out.info("starting_to_send: $it")

          channel.send(it)

          out.info("sent: $it")
        }
      } catch (e: Exception) {
        out.infoRed("sender exception: $e")
      }
    }

    val listener = launch(CoroutineName("${Emoji.MAILBOX}-listener")) {
      try {
        for (i in channel) {
          out.info("received: $i")
        }
        out.info("Listener Done - ${Emoji.CHECKERED_FLAG}")
      } catch (e: Exception) {
        out.infoRed("listener exception: $e")
      }
    }

    delay(1700)
    out.info("listener.isActive: " + listener.isActive.toString())
    out.info("Closing channel from Main")
    channel.close()
    delay(100)
    out.info("listener.isActive: " + listener.isActive.toString())

    delay(2000)
    out.info("Main completed. Exiting...")
    System.exit(0)
  }
}

class Emoji {
  companion object {
    const val LETTER = "✉\uFE0F"
    const val MAILBOX = "\uD83D\uDCEC"
    const val CHECKERED_FLAG = "\uD83C\uDFC1"
  }
}
