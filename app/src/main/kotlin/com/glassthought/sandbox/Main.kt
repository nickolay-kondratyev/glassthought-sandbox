package com.glassthought.sandbox

import com.glassthought.sandbox.util.benchmarker.Benchmarker
import com.glassthought.sandbox.util.out.impl.OutSettings
import gt.sandbox.util.output.Out
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

val out = Out.standard(OutSettings(printCoroutineName = true))

fun main(args: Array<String>) {
  runBlocking {
    val channel = Channel<Int>()
    launch(context = CoroutineName("producer")) {
      // this might be heavy CPU-consuming computation or async logic,
      // we'll just send five squares
      for (x in 1..5) {
        out.info("Sending for: $x")
        delay(500)
        channel.send(x * x)
      }
    }

    // here we print five received integers:
    repeat(5) { out.info(channel.receive().toString()) }
    println("Done!")
  }

}
