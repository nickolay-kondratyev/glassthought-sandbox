package com.glassthought.sandbox

import kotlin.concurrent.thread

fun main() {
  // Shared mutable list
  val sharedList = mutableListOf<Int>()

  // Number of threads and iterations per thread
  val threadCount = 10
  val iterations = 10000

  // Threads performing concurrent writes
  val threads = List(threadCount) { threadIndex ->
    thread {
      repeat(iterations) {
        sharedList.add(threadIndex * iterations + it) // Unsafe write
      }
    }
  }

  // Wait for all threads to finish
  threads.forEach { it.join() }

  // Analyze the result
  println("Expected size: ${threadCount * iterations}")
  println("Actual size: ${sharedList.size}")
  println("Null elements in the list count: ${sharedList.size - sharedList.filterNotNull().size}")
}
