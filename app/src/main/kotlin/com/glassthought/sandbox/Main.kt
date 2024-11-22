package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

val out = Out.standard();

fun main() = runBlocking {
  val future = CompletableFuture.supplyAsync {
    out.println("Starting to sleep")
    Thread.sleep(1000)
    out.println("Finished sleeping")
    "Hello, CompletableFuture!"
  }

  out.println("Hello, from Main thread")
  out.println(future.get()) // Output: Hello, CompletableFuture!
}
