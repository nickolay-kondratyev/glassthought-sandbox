package gt.sandbox

import gt.sandbox.util.output.Out
import kotlinx.coroutines.*

val out = Out.standard()

suspend fun fetchData(s: String) {
    out.println("Fetching data... $s")
    delay(1000) // This suspends the coroutine for 1 second without blocking the thread
    out.println("Data fetched $s")
}

fun main(): Unit = runBlocking {
    val availableCores = Runtime.getRuntime().availableProcessors()
    out.println("Number of available cores: $availableCores")

    for (i in 1..(availableCores* 2) + 2) {
        launch(Dispatchers.IO) {
            fetchData("task $i")
        }
    }
}
