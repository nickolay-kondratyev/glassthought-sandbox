package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

val out = Out.standard(outSettings = OutSettings(printColorPerThread = true))

fun printCallerStackTrace() {
  val stackTrace = Throwable().stackTrace
  // The stack trace contains an array of StackTraceElement objects
  // The first element is `Throwable`, and subsequent elements are callers
  for ((index, element) in stackTrace.withIndex()) {
    println("[$index] ${element.className}.${element.methodName} (${element.fileName}:${element.lineNumber})")
  }
}

fun callerOne() {
  callerTwo()
}

fun callerTwo() {
  printCallerStackTrace()
}

fun main(args: Array<String>) {
  callerOne()

}
