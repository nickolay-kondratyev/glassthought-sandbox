package com.glassthought.sandbox

import gt.sandbox.util.output.impl.OutSettings
import kotlinx.coroutines.*

val out = gt.sandbox.util.output.Out.standard(OutSettings(printCoroutineName = false))

fun main() = runBlocking {
  val list = List(5) { it }
  list.map {  }

}
