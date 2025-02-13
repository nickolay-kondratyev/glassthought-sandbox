package com.glassthought.sandbox

import gt.sandbox.util.output.out

fun main() {
    var counter = 0
    val numberOfThreads = 3
    val incrementsPerThread = 1000

    val threads = List(numberOfThreads) { threadNum ->
        Thread {
            for (i in 1..incrementsPerThread) {
                val current = counter
                // Simulate some processing time to increase chance of race condition
                Thread.sleep(1)
                counter = current + 1
                out.println("Thread $threadNum: counter = $counter")
            }
        }
    }

    out.printlnGreen("Starting threads...")
    threads.forEach { it.start() }
    threads.forEach { it.join() }

    out.printlnRed("\nFinal counter value: $counter")
    out.printlnBlue("Expected value: ${numberOfThreads * incrementsPerThread}")
}

