package gt.sandbox

import gt.sandbox.internal.output.Out
import kotlinx.coroutines.*

val out = Out.standard()

suspend fun fetchData(s: String) {
    out.println("Fetching data... $s")
    delay(1000) // This suspends the coroutine for 1 second without blocking the thread
    out.println("Data fetched $s")
}

fun main(): Unit = runBlocking {
    launch {
        fetchData("a-1")
        fetchData("a-2")
    }
    launch {
        fetchData("b")
    }
    launch {
        fetchData("c")
    }
}
