package com.glassthought.sandbox

import com.glassthought.sandbox.util.benchmarker.Benchmarker
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun main(args: Array<String>) {
  thread {
    runBlocking {
      val result = Benchmarker.benchmark(
        {
          Thread.currentThread().name
        },
        name = "Thread.currentThread().name",
      )

      println(result)
    }
  }
}
