package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*

val out = Out.standard()

fun main() = runBlocking {
  // If you call suspend functions without async, then the first one will
  // need to finish. Before the second one starts.
  val resultOne = fetchDataOne()
  val resultTwo =  fetchDataTwo()

  out.println("Combined result: ${resultOne + resultTwo}")
}

// Simulated data fetch function
suspend fun fetchDataOne(): Int {
  delay(1000)  // Simulate a network or heavy computation delay
  out.println("Fetched data one")
  return 10
}

// Another simulated data fetch function
suspend fun fetchDataTwo(): Int {
  delay(1500)  // Simulate a network or heavy computation delay
  out.println("Fetched data two")
  return 20
}
