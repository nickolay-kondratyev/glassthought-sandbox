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

    var stampBeforeSent = System.nanoTime()

    val sender = launch(CoroutineName("${Emoji.LETTER}-sender")) {
      repeat(100) {
        delay(50)

        out.info("starting_to_send: $it")

        stampBeforeSent = System.nanoTime()
        channel.send(it)

        out.info("sent: $it")
      }
    }

    val listener = launch(CoroutineName("${Emoji.MAILBOX}-listener")) {
      for (i in channel) {
        val stampAtReceive = System.nanoTime()


        out.info("received: $i (time: ${(stampAtReceive - stampBeforeSent) / 1000}micro-seconds)")
      }
    }

    delay(10000)
    System.exit(0)
    out.info("Main completed")
  }
}

class Emoji {
  companion object {
    const val LETTER = "✉\uFE0F"
    const val MAILBOX = "\uD83D\uDCEC"
  }
}
