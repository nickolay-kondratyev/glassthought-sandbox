package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import com.glassthought.sandbox.util.out.impl.OutSettings
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

val out = Out.standard(OutSettings(printCoroutineName = true))
suspend fun main(args: Array<String>) {
  out.info("hi")

  coroutineScope {
    launch {
      out.info("hi")
    }
  }

}
