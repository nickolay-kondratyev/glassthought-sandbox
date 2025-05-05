package com.glassthought.sandbox

import com.asgard.testTools.benchmarker.BenchMarkerJVM
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Tester(val inputVal: Int) {
  val directlyAssigned = inputVal
  val mutex = Mutex()
  val byLazy: Int by lazy {
    inputVal
  }

  fun functionCall(): Int {
    return inputVal
  }

  suspend fun functionCallWithMutex(): Int {
    return mutex.withLock {
      inputVal
    }
  }

}

var counter = 0

suspend fun main(args: Array<String>) {

  val tester = Tester(1)

  benchmark({
    val hi = tester.directlyAssigned
    counter += hi
  }, "directly assigned")

  benchmark({
    val hi = tester.byLazy
    counter += hi

  }, "directly assigned")

  benchmark({
    val hi = tester.functionCall()

    counter += hi
  }, "function call")

  benchmark({
    val hi = tester.functionCallWithMutex()

    counter += hi
  }, "function call with mutex")

}

private suspend fun benchmark(runnable: suspend () -> Unit, description: String) {
  val numberOfTimes = 10000000

  BenchMarkerJVM.benchMark(
    runnable, description = description,
    numberOfTimes = numberOfTimes
  )
    .printFormattedTable()
}
