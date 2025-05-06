package com.glassthought.sandbox

import com.asgard.testTools.benchmarker.BenchMarkerJVM
import kotlinx.coroutines.sync.Mutex

suspend fun main(args: Array<String>) {
  BenchMarkerJVM.benchMark(runnable = {
    go()
  }).printFormattedTable()
}

suspend fun go() {
  val list = mutableListOf<Mutex>()
// Loop over a range from 1 to 10000 (inclusive)
  for (i in 1..20000) {
    val element = Mutex()

    element.lock()

    list.add(element)
  }

  println(list.size)

}
