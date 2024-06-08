package gt.kotlin.sandbox

import gt.kotlin.sandbox.util.ThreadUtils
import kotlinx.coroutines.*
import java.util.stream.Collectors.toList
import java.util.stream.IntStream


suspend fun performLongRequest(msg: String): String {
    return withContext(Dispatchers.IO) {
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

}
