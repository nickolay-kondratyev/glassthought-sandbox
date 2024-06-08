import gt.kotlin.sandbox.internal.output.Out
import kotlinx.coroutines.*

val out = Out.standard()

suspend fun fetchData() {
    out.println("Fetching data...")
    delay(1000) // This suspends the coroutine for 1 second without blocking the thread
    out.println("Data fetched")
}

fun main(): Unit = runBlocking {
    launch {
        fetchData()
    }
    launch {
        fetchData()
    }
}
