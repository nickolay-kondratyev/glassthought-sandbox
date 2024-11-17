package com.glassthought.sandbox

import kotlinx.coroutines.*
import kotlin.collections.List

val out = gt.sandbox.util.output.Out.standard()

suspend fun fetchData(id: Int): String {
  // Simulate some time-consuming operation like fetching data (e.g., network request)
  delay(1000L)

  return "Data $id"
}

fun main() = runBlocking {
  // Launch multiple asynchronous tasks in parallel
  List(5) {
    async {
      // Call fetchData in parallel
      fetchData(it)
    }
  }.map { it.await() }.forEach { out.println(it) }
}
