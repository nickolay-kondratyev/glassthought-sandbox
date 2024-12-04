#### Code
```kotlin
package com.glassthought.sandbox

import gt.sandbox.util.output.Out
import com.glassthought.sandbox.util.out.impl.OutSettings
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.thread

val mutex = Mutex()

var counter = 0

suspend fun test() {
  mutex.withLock {
    counter = counter + 1
  }
}

val out = Out.standard(OutSettings(printCoroutineName = false))
val TIMES_TO_REPEAT = 100000

suspend fun main(args: Array<String>) {

  out.info("Starting on main thread")

  val t1 = thread {
    runBlocking {
      incrementInALoop("thread-1")
    }
  }

  val t2 = thread {
    runBlocking {
      incrementInALoop("thread-2")
    }
  }

  t1.join()
  t2.join()

  printResults()
}

private suspend fun incrementInALoop(threadName: String) {
  out.info("Starting execution on $threadName")
  repeat(TIMES_TO_REPEAT) {
    test()
  }
  out.info("Finished execution on $threadName")
}

private suspend fun printResults() {
  out.info("Counter : $counter")
  val expected = TIMES_TO_REPEAT * 2
  out.info("Expected: $expected")
  if (expected == counter) {
    out.printGreen("All accounted!")
    out.println("")
  } else {
    out.printRed("NOT all accounted!")
    out.println("")
  }
}
```

#### Command to reproduce:
```bash
gt.sandbox.checkout.commit badef9ec1f9c2ffed319 \
&& cd "${GT_SANDBOX_REPO}" \
&& cmd.run.announce "./gradlew run --quiet"
```

#### Recorded output of command:
```txt
[elapsed:   22ms][🥇/tname:main/tid:1] Starting on main thread
[elapsed:   57ms][⓶/tname:Thread-1/tid:21] Starting execution on thread-2
[elapsed:   57ms][⓷/tname:Thread-0/tid:20] Starting execution on thread-1
[elapsed:  422ms][⓷/tname:Thread-0/tid:20] Finished execution on thread-1
[elapsed:  423ms][⓶/tname:Thread-1/tid:21] Finished execution on thread-2
[elapsed:  425ms][🥇/tname:main/tid:1] Counter : 200000
[elapsed:  425ms][🥇/tname:main/tid:1] Expected: 200000
All accounted!
```
