package com.glassthought.sandbox

import com.glassthought.sandbox.util.benchmarker.Benchmarker
import kotlinx.atomicfu.atomic
import java.util.*

suspend fun main() {
  val atomicLong = atomic(0L)
  println(
    Benchmarker.benchmark(
      {
          val id = atomicLong.incrementAndGet()
      },
      name = "Atomic Long Counter Increment"
    )
  )

  println(
    Benchmarker.benchmark(
      {
        val string = UUID.randomUUID().toString()
      },
      name = "UUID creation"
    )
  )
}
