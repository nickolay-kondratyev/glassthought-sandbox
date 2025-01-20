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
      repeat(5) {
        delay(50)

        out.info("starting_to_send: $it")
        channel.send(it)
        out.info("sent: $it")
      }
    }

    val listener = launch(CoroutineName("${Emoji.MAILBOX}-listener")) {
      repeat(2) {
        delay(500)

        out.info("received: ${channel.receive()}")
      }
    }

    delay(3000)
    listener.cancel()
    sender.cancel()
    out.info("Main completed")
  }
}

class Emoji {
  companion object {
    const val LETTER = "✉\uFE0F"
    const val MAILBOX = "\uD83D\uDCEC"
  }
}
