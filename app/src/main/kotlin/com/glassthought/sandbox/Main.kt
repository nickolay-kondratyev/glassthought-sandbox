package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import gt.sandbox.util.output.impl.OutSettings

val out = Out.standard(outSettings = OutSettings(printColorPerThread = true))

class StackTraceRetriever {
  companion object {
    /**
     * Returns the filtered stack trace starting from the first relevant caller.
     */
    fun getStackTrace(): List<StackTraceElement> {
      // Capture the stack trace and exclude this constructor and the wrapper class
      val allStackTrace = Throwable().stackTrace
      
      return allStackTrace.dropWhile {
        it.className == StackTraceRetriever::class.java.name || it.methodName == "getStackTrace"
      }
    }
  }
}

fun callerOne() {
  callerTwo()
}

fun callerTwo() {
  val filteredStackTrace = StackTraceRetriever.getStackTrace()
  filteredStackTrace.forEachIndexed { index, element ->
    println("[$index] ${element.className}.${element.methodName} (${element.fileName}:${element.lineNumber})")
  }
}


fun main(args: Array<String>) {
  callerOne()

}
