package com.glassthought.sandbox

import gt.sandbox.util.output.out
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    val counter = AtomicInteger(0)
    val numberOfThreads = 3
    val incrementsPerThread = 1000

    val threads = List(numberOfThreads) { threadNum ->
        Thread {
            for (i in 1..incrementsPerThread) {
                // Using AtomicInteger's incrementAndGet for thread-safe increment
                val value = counter.incrementAndGet()
                out.println("Thread $threadNum: counter = $value")
            }
        }
    }

    out.printlnGreen("Starting threads...")
    threads.forEach { it.start() }
    threads.forEach { it.join() }

    out.printlnRed("\nFinal counter value: ${counter.get()}")
    out.printGreen("Expected value: ")
    out.println("${numberOfThreads * incrementsPerThread}")
}

