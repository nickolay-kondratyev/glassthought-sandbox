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

    val channel = Channel<Int>(4) // create buffered channel

    val sender = launch { // launch sender coroutine
      repeat(10) {
        delay(200)
        println("Sending $it") // print before sending each element
        channel.send(it) // will suspend when buffer is full
      }
    }

    repeat(5){
      println("Receiving ${channel.receive()}") // print after receiving each element
    }

  }

}
