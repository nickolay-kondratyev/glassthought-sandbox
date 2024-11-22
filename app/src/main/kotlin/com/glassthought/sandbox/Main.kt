package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

val out = Out.standard()

fun main() = runBlocking {
  val future = CompletableFuture.supplyAsync {
    out.println("supplyAsync")
    "Jon Snow"
  }

  out.println("thenApply().get(): " + future.thenApply {
    out.println("thenApply")
    "Hello: $it"
  }.get())

  out.println("""future.get(): [${future.get()}]""") // Output: Hello, CompletableFuture!
  println()
}
