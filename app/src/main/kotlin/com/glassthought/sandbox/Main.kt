package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
val out = Out.standard(outSettings = OutSettings(printColorPerThread = true))

fun main() = runBlocking {
  out.println("Hello, World!")

  thread{
    out.println("Hello, World! from thread-1")
  }

  thread{
    out.println("Hello, World! from thread-2")
  }

  thread{
    out.println("Hello, World! from thread-3")
  }

  out.println("Hello, World! from main")
}
