package com.glassthought.sandbox

import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture


fun main() = runBlocking {
  val future = CompletableFuture<String>()
  // Completing it programmatically
  future.complete("Result-Val")

  println("Some other work")

  println(future.get()) // Output: Result-Val
}
