package com.glassthought.sandbox

import com.glassthought.sandbox.util.benchmarker.Benchmarker
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.microseconds

suspend fun main() {

  val atomic = atomic(0L)

  val result = Benchmarker.benchmark(
    {
        atomic.incrementAndGet()
    })
  println(result)

}
