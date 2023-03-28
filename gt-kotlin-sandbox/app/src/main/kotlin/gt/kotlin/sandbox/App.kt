package gt.kotlin.sandbox

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import java.util.stream.Collectors.toList
import java.util.stream.IntStream


val threadPool = Executors.newFixedThreadPool(8)
val myDispatcher = threadPool.asCoroutineDispatcher()

suspend fun performLongRequest(msg: String): String {
    return withContext(myDispatcher) {
        ThreadUtils.printWithThreadInfo("Within subroutine (before sleep) input: $msg")

        ThreadUtils.sleep(500)

        String.format("MessageResultAfterBlockingOperation for [%s]", msg)
    }
}

fun main() {

    ThreadUtils.printWithThreadInfo("Example using async with more requests than cores on a laptop")

    val mainMillisStamp = System.currentTimeMillis();


    runBlocking {

        val deferredObjects = IntStream.range(0, 8).mapToObj { i ->
            val deferred = async { performLongRequest("${i}-request") }
            deferred
        }.collect(toList())

        for(deferred in deferredObjects) {
            ThreadUtils.printWithThreadInfo("Deferred result: " + deferred.await())
        }

        ThreadUtils.printWithThreadInfo(
            "Total time taken: " +
                    (System.currentTimeMillis() - mainMillisStamp) + "ms"
        )
    }

    myDispatcher.close();
    threadPool.shutdown();
}
