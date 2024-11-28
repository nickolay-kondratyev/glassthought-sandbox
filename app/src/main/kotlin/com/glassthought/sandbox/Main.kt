package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings
import java.util.concurrent.CompletableFuture

val out = Out.standard(outSettings = OutSettings(printColorPerThread = true))


fun main() {
  val inst1 = functionThatCreatesASingleInstance()
  val inst2 = functionThatCreatesASingleInstance()

  out.println("inst1: $inst1")
  out.println("inst2: $inst2")

  out.println("Are equal=" + (inst2 == inst1))
}
