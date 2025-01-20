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
    // Default capacity of Channel is:
    //
    // capacity: Int = RENDEZVOUS,
    val channel = Channel<Int>()

    val sender = launch(CoroutineName("${Emoji.LETTER}-sender")) {
      // In this example notice that the sender will end up getting blocked.
      repeat(2) {
        delay(100)

        out.info("starting_to_send: $it")
        channel.send(it)
        out.info("sent: $it")
      }
    }

    val listener = launch(CoroutineName("${Emoji.MAILBOX}-listener")) {
      repeat(1) {
        delay(300)

        out.info("received: ${channel.receive()}")
      }
    }

    delay(3000)
    listener.cancel()
    sender.cancel()
    out.info("Main completed")
  }
}

class Emoji{
  companion object{
    const val LETTER="✉\uFE0F"
    const val MAILBOX="\uD83D\uDCEC"
  }
}
