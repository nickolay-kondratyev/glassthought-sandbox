package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val out = Out.standard()

class MyException(msg: String) : Throwable(msg)

suspend fun main() {
  try {
    suspendCoroutine<Unit> { cont ->
      out.infoNonSuspend("About to resumeWithException")
      cont.resumeWithException(MyException("msg-from-resumeWithException"))
    }
  } catch (e: MyException) {
    out.info("Caught! exc-msg:  " + e.message)
  }
}
