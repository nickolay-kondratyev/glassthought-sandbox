package com.glassthought.sandbox

import kotlinx.coroutines.*

var counter = 0
val lock = Any()

suspend fun incrementCounter() {
  synchronized(lock) {
    val current = counter


    // Attempt to use a delay inside synchronized block
    // Compiler will not allow this with error:
    //
    // "The 'delay' suspension point is inside a critical section"
    delay(100)

    counter = current + 1
  }
}

fun main() = runBlocking {
  val jobs1 = List(1000) {
    launch(Dispatchers.Default) {
      incrementCounter()
    }
  }
  val jobs2 = List(1000) {
    launch(Dispatchers.IO) {
      incrementCounter()
    }
  }
  jobs1.forEach { it.join() }
  jobs2.forEach { it.join() }
  println("Final counter value: $counter")
}
